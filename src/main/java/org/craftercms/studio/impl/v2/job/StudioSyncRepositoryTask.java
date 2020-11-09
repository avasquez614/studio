/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.studio.impl.v2.job;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.studio.api.v1.dal.SiteFeed;
import org.craftercms.studio.api.v1.exception.ServiceLayerException;
import org.craftercms.studio.api.v1.exception.SiteNotFoundException;
import org.craftercms.studio.api.v1.exception.security.UserNotFoundException;
import org.craftercms.studio.api.v1.log.Logger;
import org.craftercms.studio.api.v1.log.LoggerFactory;
import org.craftercms.studio.api.v1.service.site.SiteService;
import org.craftercms.studio.api.v2.utils.StudioConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static org.craftercms.studio.api.v1.constant.StudioConstants.SITE_UUID_FILENAME;
import static org.craftercms.studio.api.v2.utils.StudioConfiguration.REPO_BASE_PATH;
import static org.craftercms.studio.api.v2.utils.StudioConfiguration.SITES_REPOS_PATH;

public class StudioSyncRepositoryTask extends StudioClockTask {

    private static final Logger logger = LoggerFactory.getLogger(StudioSyncRepositoryTask.class);

    protected static final Map<String, ReentrantLock> singleWorkerLockMap = new HashMap<String, ReentrantLock>();

    public StudioSyncRepositoryTask(int executeEveryNCycles,
                                    int offset,
                                    StudioConfiguration studioConfiguration,
                                    SiteService siteService) {
        super(executeEveryNCycles, offset, studioConfiguration, siteService);
    }

    @Override
    protected void executeInternal(String site) {
        try {
            try {
                syncRepository(site);
            } catch (Exception e) {
                logger.error("Failed to sync database from repository for site " + site, e);
            }
        } catch (Exception e) {
            logger.error("Failed to sync database from repository for site " + site, e);
        }
    }

    @Override
    protected boolean lockSiteInternal(String siteId) {
        ReentrantLock singleWorkerLock = singleWorkerLockMap.get(siteId);
        if (singleWorkerLock == null) {
            singleWorkerLock = new ReentrantLock();
            singleWorkerLockMap.put(siteId, singleWorkerLock);
        }
        return singleWorkerLock.tryLock();
    }

    @Override
    protected void unlockSiteInternal(String siteId) {
        ReentrantLock singleWorkerLock = singleWorkerLockMap.get(siteId);
        if (singleWorkerLock != null) {
            singleWorkerLock.unlock();
        }
    }

    private void syncRepository(String site) throws ServiceLayerException, UserNotFoundException {
        logger.debug("Getting last verified commit for site: " + site);
        SiteFeed siteFeed = siteService.getSite(site);
        if (checkSiteUuid(site, siteFeed.getSiteUuid())) {
            String lastProcessedCommit = siteService.getLastVerifiedGitlogCommitId(site);
            if (StringUtils.isNotEmpty(lastProcessedCommit)) {
                logger.debug("Syncing database with repository for site " + site + " from last processed commit "
                        + lastProcessedCommit);
                siteService.syncDatabaseWithRepo(site, lastProcessedCommit);
            }
        }
    }

    private boolean checkSiteUuid(String siteId, String siteUuid) {
        boolean toRet = false;
        try {
            Path path = Paths.get(studioConfiguration.getProperty(REPO_BASE_PATH),
                    studioConfiguration.getProperty(SITES_REPOS_PATH), siteId, SITE_UUID_FILENAME);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (!StringUtils.startsWith(line, "#") && StringUtils.equals(line, siteUuid)) {
                    toRet = true;
                    break;
                }
            }
        } catch (IOException e) {
            logger.info("Invalid site UUID. Local copy will not be deleted");
        }
        return toRet;
    }

}
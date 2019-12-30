/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.studio.impl.v2.upgrade.operations.site;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.studio.api.v1.log.Logger;
import org.craftercms.studio.api.v1.log.LoggerFactory;
import org.craftercms.studio.api.v2.exception.UpgradeException;
import org.craftercms.studio.api.v2.upgrade.UpgradeOperation;
import org.craftercms.studio.impl.v2.upgrade.operations.AbstractUpgradeOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.craftercms.studio.api.v2.upgrade.UpgradeConstants.PARAM_KEY_SITE;
import static org.craftercms.studio.api.v2.upgrade.UpgradeConstants.PARAM_KEY_VERSION;

/**
 * Base implementation of {@link UpgradeOperation} for all operations related to a XSLT template.
 *
 * <p>Supported YAML properties:
 * <ul>
 *     <li><strong>template</strong>: (required) the path to the XSLT template to apply</li>
 * </ul>
 * </p>
 *
 * @author joseross
 * @since 3.1.1
 */
public abstract class AbstractXsltFileUpgradeOperation extends AbstractUpgradeOperation {

    private static final Logger logger = LoggerFactory.getLogger(AbstractXsltFileUpgradeOperation.class);

    public static final String CONFIG_KEY_TEMPLATE = "template";

    protected static final String SAXON_CLASS = "net.sf.saxon.TransformerFactoryImpl";

    /**
     * Template file to be applied.
     */
    protected Resource template;

    public void setTemplate(final Resource template) {
        this.template = template;
    }

    @Override
    protected void doInit(final HierarchicalConfiguration<ImmutableNode> config) {
        if(template == null) {
            template = new ClassPathResource(config.getString(CONFIG_KEY_TEMPLATE));
        }
    }

    protected void executeTemplate(String site, String path, OutputStream os) throws UpgradeException {
        if(contentRepository.contentExists(site, path)) {
            try(InputStream templateIs = template.getInputStream()) {
                // Saxon is used to support XSLT 2.0
                Transformer transformer =
                    TransformerFactory.newInstance(SAXON_CLASS, null)
                        .newTransformer(new StreamSource(templateIs));
                logger.info("Applying XSLT template {0} to file {1} for site {2}", template, path, site);
                try(InputStream sourceIs = contentRepository.getContent(site, path)) {
                    transformer.setParameter(PARAM_KEY_SITE, site);
                    transformer.setParameter(PARAM_KEY_VERSION, nextVersion);
                    transformer.transform(new StreamSource(sourceIs), new StreamResult(os));
                }
            } catch (Exception e) {
                throw new UpgradeException("Error processing file", e);
            }
        } else {
            logger.warn("Source file {0} does not exist in site {1}", path, site);
        }
    }

}

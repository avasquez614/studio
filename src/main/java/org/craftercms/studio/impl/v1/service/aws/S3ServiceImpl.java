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

package org.craftercms.studio.impl.v1.service.aws;

import java.io.InputStream;

import org.craftercms.commons.validation.annotations.param.ValidateStringParam;
import org.craftercms.studio.api.v1.aws.s3.S3Output;
import org.craftercms.studio.api.v1.aws.s3.S3Profile;
import org.craftercms.studio.api.v1.exception.AwsException;
import org.craftercms.studio.api.v1.service.aws.AbstractAwsService;
import org.craftercms.studio.api.v1.service.aws.S3Service;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Default implementation of {@link S3Service}.
 *
 * @author joseross
 */
public class S3ServiceImpl extends AbstractAwsService<S3Profile> implements S3Service {

    protected int partSize;

    public S3ServiceImpl() {
        partSize = AwsUtils.MIN_PART_SIZE;
    }

    public void setPartSize(final int partSize) {
        this.partSize = partSize;
    }

    protected AmazonS3 getS3Client(S3Profile profile) {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(profile.getCredentials()))
            .withRegion(profile.getRegion())
            .build();
    }

    @Override
    public S3Output uploadFile(@ValidateStringParam(name = "site") String site,
                               @ValidateStringParam(name = "profileId") String profileId,
                               @ValidateStringParam(name = "filename") String filename,
                               InputStream content) throws AwsException {
        S3Profile profile = getProfile(site, profileId);
        AmazonS3 s3Client = getS3Client(profile);
        String inputBucket = profile.getBucketName();
        String inputKey = filename;

        AwsUtils.uploadStream(inputBucket, inputKey, s3Client, partSize, filename, content);

        S3Output output = new S3Output();
        output.setBucket(inputBucket);
        output.setKey(inputKey);
        return output;
    }

}

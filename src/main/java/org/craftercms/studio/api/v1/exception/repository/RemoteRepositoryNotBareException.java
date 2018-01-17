/*
 * Crafter Studio Web-content authoring solution
 * Copyright (C) 2007-2018 Crafter Software Corporation.
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

package org.craftercms.studio.api.v1.exception.repository;

public class RemoteRepositoryNotBareException extends Exception {

    private static final long serialVersionUID = 3989253201447916999L;

    public RemoteRepositoryNotBareException() {
    }

    public RemoteRepositoryNotBareException(String message) {
        super(message);
    }

    public RemoteRepositoryNotBareException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteRepositoryNotBareException(Throwable cause) {
        super(cause);
    }

    public RemoteRepositoryNotBareException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

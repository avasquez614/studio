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

package org.craftercms.studio.model.search;

import java.util.Map;

/**
 * Holds the data needed to perform a search operation
 * @author joseross
 */
public class SearchParams {

    /**
     * Keywords to search in the files.
     */
    protected String keywords;

    /**
     * Lucene query to execute.
     */
    protected String query;

    /**
     * The offset to paginate the results
     */
    protected int offset = 0;

    /**
     * The limit to paginate the results
     */
    protected int limit = 20;

    /**
     * The field to sort the results
     */
    protected String sortBy = "_score";

    /**
     * The order to sort the results
     */
    protected String sortOrder = "DESC";

    /**
     * The filters to search the files
     */
    protected Map<String, Object> filters;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(final String keywords) {
        this.keywords = keywords;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(final String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(final Map<String, Object> filters) {
        this.filters = filters;
    }

}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.robot.sdk.android.oss.model;

import java.io.Serializable;

/**
 * OSS Bucket owner class definition
 */
public class Owner implements Serializable {

    private static final long serialVersionUID = -1942759024112448066L;
    private String displayName;
    private String id;

    /**
     * Constructor
     */
    public Owner() {
        this(null, null);
    }

    /**
     * Constructor
     *
     * @param id          Owner Id
     * @param displayName Display name
     */
    public Owner(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /**
     * Returns the serialization string.
     */
    @Override
    public String toString() {
        return "Owner [name=" + getDisplayName() + ",id=" + getId() + "]";
    }

    /**
     * Gets the owner Id
     *
     * @return The owner Id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the owner Id
     *
     * @param id The owner Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the owner's display name
     *
     * @return The owner's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the owner's display name
     *
     * @param name The owner's display name
     */
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    /**
     * Checks if 'this' object is same as the specified one.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Owner)) {
            return false;
        }

        Owner otherOwner = (Owner) obj;

        String otherOwnerId = otherOwner.getId();
        String otherOwnerName = otherOwner.getDisplayName();
        String thisOwnerId = this.getId();
        String thisOwnerName = this.getDisplayName();

        if (otherOwnerId == null) otherOwnerId = "";
        if (otherOwnerName == null) otherOwnerName = "";
        if (thisOwnerId == null) thisOwnerId = "";
        if (thisOwnerName == null) thisOwnerName = "";

        return (otherOwnerId.equals(thisOwnerId) &&
                otherOwnerName.equals(thisOwnerName));
    }

    /**
     * Gets the hash code of the 'this' object
     */
    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return 0;
        }
    }

}

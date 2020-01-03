/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.cli.profile

/**
 * A {@link Command} applicable only to a certain {@link Profile}
 *
 * @author Graeme Rocher
 * @since 1.0
 */
interface ProfileCommand extends Command {
    /**
     * @return The profile of the command
     */
    Profile getProfile()

    /**
     * Sets the command profile
     *
     * @param profile The profile
     */
    void setProfile(Profile profile)

}
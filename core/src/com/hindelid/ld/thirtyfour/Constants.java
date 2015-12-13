/*******************************************************************************
 * Copyright 2014 Christoffer Hindelid. http://www.hindelid.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.hindelid.ld.thirtyfour;

import java.util.Random;

/**
 * Created by chris on 12 Dec 2015.
 */
public interface Constants {

    int VIEW_SIZE_X = 4;
    int VIEW_SIZE_Y = 4;

    float SHRINKAGE_FACTOR = 0.25f; // :)
    float SPEED = 0.05f;

    Random sRandom = new Random();
}


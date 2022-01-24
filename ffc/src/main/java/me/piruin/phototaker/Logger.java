/*
 * Copyright (c) 2016 Piruin Panichphol
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.piruin.phototaker;

import android.util.Log;

final class Logger {
  private static final String TAG = "PhotoTaker";

  private Logger() {
  }

  static void log(String msgLog) {
//    if (BuildConfig.DEBUG) Log.d(TAG, msgLog);
  }

  static void log(String logFormat, String... args) {
//    if (BuildConfig.DEBUG) Log.d(TAG, String.format(logFormat, args));
  }
}

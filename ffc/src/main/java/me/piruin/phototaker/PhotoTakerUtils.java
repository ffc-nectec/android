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

import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PhotoTakerUtils {

  private PhotoTakerUtils() {
  }

  public static boolean writeBitmapToFile(@NonNull Bitmap bitmap, @NonNull File file) {
    try (FileOutputStream fops = new FileOutputStream(file);) {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fops);
      fops.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}

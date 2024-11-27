package com.harisfi.listo.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.harisfi.listo.R
import java.io.IOException
import java.io.InputStream
import java.util.Properties

object Helper {
    private const val TAG = "Helper"

    fun getConfigValue(context: Context, name: String): String? {
        val resources: Resources = context.resources

        return try {
            val rawResource: InputStream = resources.openRawResource(R.raw.config)
            val properties = Properties()
            properties.load(rawResource)
            properties.getProperty(name)
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find the config file: ${e.message}")
            null
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open config file.")
            null
        }
    }
}

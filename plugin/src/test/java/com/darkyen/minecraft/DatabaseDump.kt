package com.darkyen.minecraft

import com.darkyen.minecraft.database.SoulDatabase
import com.darkyen.minecraft.utils.serialization.Serialization
import java.io.IOException
import java.nio.file.Paths

/**
 *
 */
object DatabaseDump {
    @Throws(IOException::class, Serialization.Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        SoulDatabase(null, Paths.get("/Users/Darkyen/Downloads/soul-db.bin"))
    }
}

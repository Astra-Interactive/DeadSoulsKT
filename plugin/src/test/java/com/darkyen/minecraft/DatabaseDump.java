package com.darkyen.minecraft;

import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.utils.serialization.Serialization;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 */
public class DatabaseDump {

	public static void main(String[] args) throws IOException, Serialization.Exception {
		new SoulDatabase(null, Paths.get("/Users/Darkyen/Downloads/soul-db.bin"));
	}

}

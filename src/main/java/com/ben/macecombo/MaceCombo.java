package com.ben.macecombo;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaceCombo implements ModInitializer {
	public static final String MOD_ID = "macecombo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Mace Combo loaded!");
	}
}

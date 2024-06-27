package com.ridanisaurus.emendatusenigmatica.plugin.deposit;

import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonDepositModelBase;

import java.util.List;

public interface IDepositProcessor {
	void load();
	String getType();
	CommonDepositModelBase getCommonModel();

	List<CommonBlockDefinitionModel> getBlocks();

	int getChance();
	int getSize();
	int getMaxY();
	int getMinY();
	boolean hasSurfaceSample();

	String getPlacement();

	String getRarity();
}

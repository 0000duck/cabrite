package com.imm.bean;

import java.io.IOException;

import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public interface iMode {
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException;
	public void refresh() throws VartypeException, VarNotExistException, IOException, NetworkException;
}

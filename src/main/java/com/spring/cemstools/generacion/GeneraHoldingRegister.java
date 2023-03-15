package com.spring.cemstools.generacion;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class GeneraHoldingRegister extends GeneraGeneral{
	
	public static String formateaString(List<String> info) {
		String res="";
		boolean coils = false;
		boolean finCoils = false;
		boolean inputs = false;
		boolean finInputs = false;
		boolean eanal = false;
		boolean finEanal = false;
		boolean wo = false;
		boolean finWo = false;
		
		for (String linea : info) {
			
			if(linea.contains("COILS")) {
				coils = true;
				
				res+="//**************************\n";
				res+="// COILS\n";
				res+="//**************************\n\n";
				
			} if(linea.trim().startsWith("WO")) {
				wo = true;
				
				res+="//**************************\n";
				res+="// ORDENES\n";
				res+="//**************************\n\n";
				
			}else if(linea.contains("DIGITAL_INPUTS")) {
				inputs = true;
				
				res+="//**************************\n";
				res+="// Entradas Digitales\n";
				res+="//**************************\n\n";
				
			}else if(linea.trim().startsWith("EA_") && !eanal) {
				eanal = true;
				
				res+="//**************************\n";
				res+="// Entradas Analogicas\n";
				res+="//**************************\n\n";
				
			} else if(coils && wo && linea.contains("END_STRUCT") && !finCoils && !finWo) {
				finWo = true;
			} else if(coils && linea.contains("END_STRUCT") && !finCoils) {
				finCoils = true;
			} else if(inputs && linea.contains("END_STRUCT") && !finInputs) {
				finInputs = true;
			} else if(eanal && linea.contains("END_STRUCT") && !finEanal) {
				finEanal = true;
			}
			
			if(coils && !finCoils && !linea.contains("COILS") && wo && !finWo) {
				res+=stringToWo(linea)+"\n\n";
			}else if(coils && !finCoils && !linea.contains("COILS")) {
				res+=stringToCoils(linea)+"\n\n";
			}else if(inputs && !finInputs && !linea.contains("DIGITAL_INPUTS") && linea.trim().startsWith("ED_")) {
				res+=stringToDInputs(linea)+"\n\n";
			} else if(eanal && !finEanal && linea.trim().startsWith("EA_")) {
				res+=stringToEAnal(linea)+"\n\n";
			}
		}
		
		return res;
	}
	
	
	//Genera fichero con el bloque addAlarmaDigital generado a partir de un fichero de alarmas del PLC
	public static void generar(String origen, String destino) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(origen));
			BufferedWriter bw = new BufferedWriter(new FileWriter(destino));
			String linea;
			boolean coils = false;
			boolean finCoils = false;
			boolean inputs = false;
			boolean finInputs = false;
			boolean eanal = false;
			boolean finEanal = false;
			boolean wo = false;
			boolean finWo = false;
			

			while((linea = br.readLine())!= null) {
				
				if(linea.contains("COILS")) {
					coils = true;
					
					bw.write("//**************************");
					bw.newLine();
					bw.write("// COILS");
					bw.newLine();
					bw.write("//**************************");
					bw.newLine();
					bw.newLine();
					
				} if(linea.trim().startsWith("WO")) {
					wo = true;
					
					bw.write("//**************************");
					bw.newLine();
					bw.write("// ORDENES");
					bw.newLine();
					bw.write("//**************************");
					bw.newLine();
					bw.newLine();
					
				}else if(linea.contains("DIGITAL_INPUTS")) {
					inputs = true;
					
					bw.write("//**************************");
					bw.newLine();
					bw.write("// Entradas digitales");
					bw.newLine();
					bw.write("//**************************");
					bw.newLine();
					bw.newLine();
					
				}else if(linea.trim().startsWith("EA_") && !eanal) {
					eanal = true;
					
					bw.write("//**************************");
					bw.newLine();
					bw.write("// Entradas analogicas");
					bw.newLine();
					bw.write("//**************************");
					bw.newLine();
					bw.newLine();
					
				} else if(coils && wo && linea.contains("END_STRUCT") && !finCoils && !finWo) {
					finWo = true;
				} else if(coils && linea.contains("END_STRUCT") && !finCoils) {
					finCoils = true;
				} else if(inputs && linea.contains("END_STRUCT") && !finInputs) {
					finInputs = true;
				} else if(eanal && linea.contains("END_STRUCT") && !finEanal) {
					finEanal = true;
				}
				
				if(coils && !finCoils && !linea.contains("COILS") && wo && !finWo) {
					bw.write(stringToWo(linea));
					bw.newLine();
					bw.newLine();
				}else if(coils && !finCoils && !linea.contains("COILS")) {
					bw.write(stringToCoils(linea));
					bw.newLine();
					bw.newLine();
				}else if(inputs && !finInputs && !linea.contains("DIGITAL_INPUTS") && linea.trim().startsWith("ED_")) {
					bw.write(stringToDInputs(linea));
					bw.newLine();
					bw.newLine();
				} else if(eanal && !finEanal && linea.trim().startsWith("EA_")) {
					bw.write(stringToEAnal(linea));
					bw.newLine();
					bw.newLine();
				}

			}
			
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error en la lectura del fichero origen:"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error en el cierre del fichero origen :"+e.toString());
			e.printStackTrace();
		}
		
	}
	
	private static String stringToWo(String linea) {
		String res = "addSenalInstDigital(map, entorno, TipoAcceso.ESCRITURA, "
				+"		CAT_D_NINGUNA, CAT_D_NINGUNA, imagenProceso, prf, "
				+"		X_TAGN_X, X_DESC_X, zona, "
				+"		GrupoOrganizacion.NINGUNO, GrupoDibujado.NINGUNO, GrupoHistorico.NINGUNO," 
				+"		getValorDigital(map, prf, X_TAG_X));";
		
		String tag = getTag(linea);
		String desc = traduceDesc(getDesc(linea));
		String tagn = tag.replace("M_", "CMD");
		
		res = res.replaceFirst("X_TAG_X", tag);
		res = res.replaceFirst("X_TAGN_X", tagn);
		res = res.replaceFirst("X_DESC_X", desc);
		
		return res;
	}

	//En base a una linea de texto del fichero en bruto genera una linea de una señal entrada analogica
	private static String stringToEAnal(String linea) {
		String res ="addSenalInstAnalogica(map, entorno,   TipoAcceso.LECTURA, null,\n"
				+"		X_MAXMIN_X,\n"
				+"		entorno.getFormato0_00(), X_CAT1_X, X_CAT2_X, imagenProceso, \n"
				+"		prf, \"X_TAG_X\", X_DESC_X, Unknown, zona,\n"
				+"		GrupoOrganizacion.NINGUNO, GrupoDibujado.NINGUNO, GrupoHistorico.NINGUNO, X_PCT_VALID_X);";
		
		String tag = getTag(linea);
		String desc = getDesc(linea);
		String maxMin = getMaxMin(tag,tag.replaceFirst("EA_", ""));
		
		
		
		if(tag.length()>=1) {
			res = res.replaceFirst("X_TAG_X", tag);
			res = res.replaceFirst("X_MAXMIN_X", maxMin);
			res = res.replaceFirst("X_DESC_X", traduceDesc(desc));
			
			if(tag.endsWith("_XX")) {
				res = res.replaceFirst("X_PCT_VALID_X", "-100");
				res = res.replaceFirst("X_CAT1_X", "TipoCatFct.NINGUNA");
				res = res.replaceFirst("X_CAT2_X", "TipoCatFct.NINGUNA");
			}else {
				res = res.replaceFirst("X_PCT_VALID_X", "PCT_VALID");
				res = res.replaceFirst("X_CAT1_X", "TipoCatFct.MARCHA");
				res = res.replaceFirst("X_CAT2_X", "TipoCatFct.ALARMA");
			}
			
		}
		
		return res;
	}

	//Devuelve un string con el codigo de los margenes y minimos de la señal analogica
	private static String getMaxMin(String tag,String elem) {
		String res = "";
		
		if(tag.endsWith("_MA")) {
			res = "creaMinMaxMA()";
		}else if(tag.endsWith("_XX")){
			res = "null";
		}else {
			if(!elem.contains("UNKNOWN")) {
			res = "new MinMaxTags(\n" + 
					"			getValorAnalogico(map, prf, \"MIN_"+elem+"\"),\n" + 
					"			getValorAnalogico(map, prf, \"MAX_"+elem+"\"),\n" + 
					"			indepActualiz)";
			}else {
				res = "new MinMaxTags(\n" + 
						"			getValorAnalogico(map, prf, UNKNOWN ),\n" + 
						"			getValorAnalogico(map, prf, UNKNOWN ),\n" + 
						"			indepActualiz)";
			}
		}
		
		return res;
	}
	
	//En base a una linea de texto del fichero en bruto genera una linea de una señal entrada digital
	private static String stringToDInputs(String linea) {
		String res = "addSenalInstDigital(map, entorno, TipoAcceso.LECTURA, \n"
				+"		X_CAT1_X, X_CAT2_X, imagenProceso, prf, \"X_TAG_X\", X_DESC_X, zona, \n"
				+"		GrupoOrganizacion.UNKNOW, GrupoDibujado.NINGUNO, GrupoHistorico.NINGUNO, \n"
				+"		null);";
		
		String[] values = traduceTagDI(getTag(linea));
		String desc = traduceDesc(getDesc(linea));
		
		if(values[0].length()>=1) {
			res = res.replaceFirst("X_TAG_X", values[0]);
			res = res.replaceFirst("X_CAT1_X", values[1]);
			res = res.replaceFirst("X_CAT2_X", values[2]);
			res = res.replaceFirst("X_DESC_X", desc);
		}
		
		return res;
	}
	
	//Devuleve un array con el valor del nuevo tag DI, su categoria 1 y categoria 2 
	private static String[] traduceTagDI(String tag) {
		String[] res = {"","",""};
		
		if(tag.contains("ERROR")) {
			res[0] = tag;
			res[1] = "CAT_D_FALLO_GENERICO";
			res[2] = "CAT_D_NINGUNA";
			
		}else if(tag.contains("MARCHA")) {
			res[0] = tag;
			res[1] = "CAT_D_MARCHA";
			res[2] = "CAT_D_NINGUNA";
			
		}else {
			res[0] = tag;
			res[1] = "UNKNOW";
			res[2] = "UNKNOW";
		}
			
		if(tag.contains("INV")) {
			String aux = res[1];
			
			res[0] = tag;
			res[1] = res[2];
			res[2] = aux;
		}
		
			
		return res;
	}
	
	//En base a una linea de texto del fichero en bruto genera una linea de una señal COIL
	private static String stringToCoils(String linea) {
		String res = "addSenalInstDigital(map, entorno, TipoAcceso.LECTURA_ESCRITURA, \n"
				+"		X_CAT1_X, X_CAT2_X, imagenProceso, prf, \n"
				+"		\"X_TAG_X\", X_DESC_X, zona, \n"
				+"		GrupoOrganizacion.NINGUNO, GrupoDibujado.NINGUNO, GrupoHistorico.NINGUNO, \n"
				+"		X_HAB_X);";
		
		String[] values = traduceTagCoils(getTag(linea));
		String desc = traduceDesc(getDesc(linea));
		
		if(values[0].length()>=1) {
			res = res.replaceFirst("X_TAG_X", values[0]);
			res = res.replaceFirst("X_CAT1_X", values[1]);
			res = res.replaceFirst("X_CAT2_X", values[2]);
			res = res.replaceFirst("X_HAB_X", values[3]);
			res = res.replaceFirst("X_DESC_X", desc);
		}
		
		return res;
	}
	
	//Devuleve un array con el valor del nuevo tag coil, su categoria 1, categoria 2 y su habilitador
	private static String[] traduceTagCoils(String tag) {
		String[] res = {"","","",""};
		String[] cat = getCategoria(tag);
		
		if(tag.startsWith("SD_")) {
			res[0] = tag;
			res[1] = cat[0];
			res[2] = cat[1];
			res[3] = "cemsManual";
			
		}else if(tag.startsWith("VRF_") || tag.startsWith("LIN_")) {
			res[0] = "USR_"+tag;
			res[1] = cat[0];
			res[2] = cat[1];
			res[3] = "cemsAuto";
			
		}else {
			res[0] = "USR_"+tag;
			res[1] = cat[0];
			res[2] = cat[1];
			res[3] = "null";
		}
		
		return res;
	}
	
	//DEvuleve las categorias asociadas al nombre del tag
	private static String[] getCategoria(String tag) {
		String[] res = {"",""};
		
		if(tag.contains("ERROR")) {
			res[0] = "CAT_D_FALLO_GENERICO";
			res[1] = "CAT_D_NINGUNA";
		}else if(tag.contains("AVS")) {
			res[0] = "CAT_D_AVISO_GENERICO";
			res[1] = "CAT_D_NINGUNA";			
		}else if (tag.contains("MARCHA")) {
			res[0] = "CAT_D_MARCHA";
			res[1] = "CAT_D_NINGUNA";
		}else {
			res[0] = "UNKNOW";
			res[1] = "UNKNOW";
		}
			
		return res;
	}


}

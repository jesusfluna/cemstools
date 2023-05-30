package com.spring.cemstools.generacion;


import java.util.List;


public class GeneraMMPlcLocalWeb extends GeneraGeneral{
	static int inicio = 40001;

	
	
	public static String formateaString(List<String> info, List<String> infoAlrm ) {
		String res="";
		boolean coils = false;
		boolean finCoils = false;
		boolean struct = false;
		boolean finStructs = false;
		int actual = inicio;
		int aux = inicio;

		
		for (String linea : info) {
			if(linea.contains("COILS")) {
				coils = true;
				
				res += "cargaTags( \r\n"
					+ "	new ListaTagsConTipo[] {\r\n"
					+ "		new ListaTagsConTipo(\r\n"
					+ " 			TipoDatoCfg.BOOL_ES,\r\n"
					+ "			new String[] {";
			} else if(coils && linea.contains("END_STRUCT") && !finCoils) {
				finCoils = true;
				res += completarBloque(inicio,actual) 
					+ "			}\r\n"
					+ "		),\n";
				actual += (16-((actual-inicio)%16));
			}else if(linea.contains("DIGITAL_INPUTS")) {
				struct = true;
				
				res+="		new ListaTagsConTipo(\r\n"
						+ "			TipoDatoCfg.BOOL,\r\n"
						+ "			new String[] {";
				
			}else if(struct && linea.contains("END_STRUCT") && !finStructs) {
				finStructs = true;
				res += completarBloque(inicio,actual) 
						+ "				}\r\n"
						+ " 			),\r\n"
						+ "		},\r\n"
						+ "		mapa, \r\n"
						+ "		prf, \r\n"
						+ "		plc,        		\r\n"
						+ "		new int[] { \r\n"
						+ "		"+inicio+", \r\n"
						+ "		1\r\n"
						+ "		},\r\n"
						+ "		0\r\n"
						+ "	);";
				actual += completarBloque(inicio, actual).split("null").length-1;
				aux = (inicio+((actual-inicio)/16));
			}
			
			
			if(coils && !finCoils && !linea.contains("COILS")) {
				
				if((actual-inicio)%16==0) {
					res += "\n				//"+(inicio+((actual-inicio)/16))+"\n";
				}
				res += "				"+traduceTag(getTag(linea))+",\n";
				actual++;
			}else if(struct && !finStructs && !linea.contains("DIGITAL_INPUTS") && linea.trim().startsWith("ED_")) {
				if((actual-inicio)%16==0) {
					res += "\n				//"+(inicio+((actual-inicio)/16))+"\n";
				}
				res += "				"+traduceTag(getTag(linea))+",\n";
				actual++;
			}
			
		}
		
		struct = false;
		finStructs = false;
		
		for (String linea : infoAlrm) {
			if(linea.contains("STRUCT") && !struct && !finStructs) {
				struct = true;
				
				res += "\ncargaTags( \r\n"
						+ "	new ListaTagsConTipo[] {\r\n"
						+ "		new ListaTagsConTipo(\r\n"
						+ "			TipoDatoCfg.BOOL_ALM,\r\n"
						+ "			new String[] {\n";
				
			}else if(struct && linea.contains("END_STRUCT;") && !finStructs) {
				finStructs = true;
				int blq = ((actual-aux)/16);
				
				res += completarBloque(inicio,actual);
				res +=  "				}\r\n"
						+ "			),\r\n"
						+ "		},\r\n"
						+ "		mapa, \r\n"
						+ "		prf, \r\n"
						+ "		plc,        		\r\n"
						+ "		new int[] { "+aux+", 1 },\r\n"
						+ "		"+blq+"\r\n"
						+ "	);\n";
				
			}else if(struct && !finStructs) {
				if(linea.length()>2) {
					if((actual-inicio)%16==0) {
						res += "\n				//"+(inicio+((actual-inicio)/16))+"\n";
					}
					
					if(linea.contains("NO_USADA")) {
						res += "				null,\n";
					}else {
						res += "				"+traduceTag(getTag(linea))+",\n";
					}
					
					actual++;
				}
			}
		}
		
		return res;
	}
	

	private static String traduceTag(String tag) {
		String res = tag;
		
		if(!tag.startsWith("SD_") && !tag.startsWith("ED_")) {
			res ="USR_"+tag;
		}
		
		if(tag.endsWith("_M")) {
			res = tag.substring(0, tag.length()-2);
		}

		return "\""+res+"\"";
	}
	
	private static String completarBloque(int inicio, int actual) {
		String res = "";
		int act = (actual-inicio)%16;
		
		
		for(int i=act; i < 16 ; i++) {
			res +="				null,\n";
			actual++;
		}
		
		
		return res;
	}
}

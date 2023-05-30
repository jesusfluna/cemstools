package com.spring.cemstools.generacion;


import java.util.List;

public class GeneraTagsCfgWeb extends GeneraGeneral{
	
	public static String formateaString(List<String> info) {
		String res="";
		boolean struct= false;
		boolean endStrunct = false;
		
		for (String linea : info) {
			
			if(struct && linea.contains("END_STRUCT") && !endStrunct) {
				endStrunct = true;
			}else if(linea.contains("STRUCT")) {
				struct = true;
				
				res+="//**************************\n";
				res+="// TAGS CFG WEB\n";
				res+="//**************************\n\n";
				res+=getInicio();
				
			}else if(struct && !endStrunct) {
				res+= "						  "+getTag(linea)+",\n";
			}
		}
		
		return res+getCierre();
	}
	
	private static String getInicio() {
		return "public class TagsCfgWeb {\r\n"
				+ "	public static final ListaTagsConTipo[] getInstance(boolean barahona) {\r\n"
				+ "		return new ListaTagsConTipo[] {\r\n"
				+ "			new ListaTagsConTipo(\r\n"
				+ "					TipoDatoCfg.REAL,\r\n"
				+ "					new String[] {\n";
	}
	
	private static String getCierre() {
		return "						  }\r\n"
				+ "			)\r\n"
				+ "		};\r\n"
				+ "	}\r\n"
				+ "}\n";
	}
}

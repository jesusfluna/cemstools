package com.spring.cemstools.generacion;


public class GeneraGeneral{
	
	//Devolvemos la constante contenida en una descripcion SIN formatear
		protected static String getGrupo(String desc) {
			String res = "UNKNOWN";
			
			if(desc.contains("@")) {
				res = desc.substring(desc.indexOf("@")+1,desc.length());
				res = res.substring(0,res.indexOf("@")).toUpperCase();
			}
			
			return res;
		}
		
		//Devuelve el TAG de una linea de texto
		protected static String getTag(String linea) {
			String res = "";
			
			if(linea.contains("{")) {
				res = linea.substring(0,linea.indexOf('{')-1).trim();
			}
			
			return res;
		}
		
		//Devuleve la descripcion de una linea de texto
		protected static String getDesc(String linea) {
			String res = "";
			
			if(linea.contains("//")) {
				res =linea.substring(linea.indexOf("//")+2, linea.length()).trim();
			}
			
			return res;
		}
		
		//Si aparece la anotacion @XXX@ cambia las @ por la apertura y cierre de concatenacion de texto
		protected static String traduceDesc(String desc) {
			String res = desc.trim();
			if(desc.contains("@")) {
				
				if(res.startsWith("@")) {
					res = res.replaceFirst("@", "");
					
					if(res.contains("@") && res.indexOf("@")==res.length()-1) {
						res = res.replaceFirst("@", "");
						return res = "\""+res;
					}else{
						res = res.replaceFirst("@", "+\"");
						return res = res+"\"";
					}
					
				}else {
					res = res.replaceFirst("@", "\"+");
					
					if(res.contains("@") && res.indexOf("@")==res.length()-1) {
						res = res.replaceFirst("@", "");
						return res = "\""+res;
					}else{
						res = res.replaceFirst("@", "+\"");
						return res = "\""+res+"\"";
					}
				}
			}
			
			return "\""+res+"\"";
		}
}

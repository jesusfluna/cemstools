package com.spring.cemstools.generacion;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneraAlarmas extends GeneraGeneral{
	
	public static String formateaString(List<String> datos) {
		String res = "";
		int contAlrm = 0;
		
		for (String linea : datos) {
			if(linea.contains("DESBORD_COLA_ERROR_M")) {
				System.out.println("");
			}
			
			if(linea.length()>20) {
				if(contAlrm % 16 == 0) {
					res+="//Digitales "+ ((contAlrm / 16)+1)+"\n\n";
					res+="assert(enlazador.primerBit());\n\n";
				}
				
				res+=stringToAlarma(linea)+"\n\n";
				contAlrm++;
			}
		}
		
		res+="enlazador.saltaAlarmas("+(16-(contAlrm%16))+");\n";
		res+="assert(enlazador.fin());\n";
		
		return res;
	}

	//Genera fichero con el bloque addAlarmaDigital generado a partir de un fichero de alarmas del PLC
	public static void generar(String origen, String destino) {
		int contAlrm = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(origen));
			BufferedWriter bw = new BufferedWriter(new FileWriter(destino));
			String linea;
			
			while((linea = br.readLine())!= null) {
				
				if(linea.length()>20) {
					if(contAlrm % 16 == 0) {
						bw.write("//Digitales "+ ((contAlrm / 16)+1));
						bw.newLine();
						bw.newLine();
						bw.write("assert(enlazador.primerBit());");
						bw.newLine();
						bw.newLine();
					}
					
					bw.write(stringToAlarma(linea));
					bw.newLine();
					bw.newLine();
					
					contAlrm++;
				}
			}
			
			bw.write("enlazador.saltaAlarmas("+(16-(contAlrm%16))+");");
			bw.newLine();
			bw.write("assert(enlazador.fin());");
			bw.newLine();
			
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
	
	
	//En base a una linea de texto del fichero en bruto genera una linea de codigo addAlarmaDigital
	public static String stringToAlarma(String linea) {
		String res = "";
		
		if(linea.length()>20) {
			System.out.println(linea);
			res = "addAlarmaDigital(grupoAlarmas, mapa, entorno, TipoAcceso.LECTURA, \n"
					+"enlazador, false, indepActualiz, X_CAT_X, CAT_D_NINGUNA, imagenProceso, \n"
					+"prf, X_TAG_X, X_DESC_X, zona, \n"
					+"GruposOrganizacion.X_GRUPO_X_DIGITALES, GruposDibujado.DIGITALES, GruposHistoricos.ALARMAS);";
			
			String tag = traduceTag(getTag(linea));
			String desc = getDesc(linea);
			
			if(getTagCategoria(tag).length()>0) {
				res = res.replaceFirst("X_TAG_X", "\""+tag+"\"");
				res = res.replaceFirst("X_DESC_X", traduceDesc(desc));
				res = res.replaceFirst("X_CAT_X", getTagCategoria(tag));
				res = res.replaceFirst("X_GRUPO_X", getGrupo(getDesc(linea)));
			}else{
				res = "enlazador.siguienteAlarma();";
			}
		}
	
		return res;
	}
	
	
	
	
	//Quita el sufijo '_M' del nombre del tag
	private static String traduceTag(String tag) {
		if(tag.endsWith("_M")) {
			return tag.substring(0, tag.length()-2);
		}else {
			return tag;
		}
	}
	
	//Devuelve el Tag de categoria de un tag (ERROR o AVISO)
	private static String getTagCategoria(String tag) {
		String res = "";
		if(tag.contains("ERROR")) {
			res = "CAT_D_FALLO_GENERICO";
		}else if(tag.contains("AVS")) {
			res = "CAT_D_AVISO_GENERICO";
		}
			
		return res;
	}
}

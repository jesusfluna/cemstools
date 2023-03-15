package com.spring.cemstools.generacion;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneraCfgWeb extends GeneraGeneral{
	
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
				res+="// CFG Web\n";
				res+="//**************************\n\n";
			}else if(struct && !endStrunct) {
				res+=stringToWebCfg(linea)+"\n\n";
			}
		}
		
		return res;
	}
	
	
	//Genera fichero con el bloque addSenalConfiguracion generado a partir de un fichero bruto txt del PLC
	public static void generar(String origen, String destino) {
		boolean struct= false;
		boolean endStrunct = false;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(origen));
			BufferedWriter bw = new BufferedWriter(new FileWriter(destino));
			String linea;
			
			while((linea = br.readLine())!= null) {
				
				if(struct && linea.contains("END_STRUCT") && !endStrunct) {
					endStrunct = true;
				}else if(linea.contains("STRUCT")) {
					struct = true;
					
					bw.write("//**************************");
					bw.newLine();
					bw.write("// CFG Web");
					bw.newLine();
					bw.write("//**************************");
					bw.newLine();
					bw.newLine();
				}else if(struct && !endStrunct) {
					
					bw.write(stringToWebCfg(linea));
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

	//En base a una linea de texto del fichero en bruto genera una linea de codigo addSenalConfiguracion
	private static String stringToWebCfg(String linea) {
		String res = "addSenalConfiguracion(map, entorno, TipoAcceso.LECTURA, null, \r\n" + 
				"				null, null, entorno.getFormato0_00(), TipoCatFct.NINGUNA, TipoCatFct.ALARMA, imagenProceso, prf,\r\n" + 
				"				X_TAG_X, X_DESC_X, Unknown, zona, \r\n" + 
				"				GruposOrganizacion.X_GRUPO_X_ANALOGICAS, GrupoDibujado.NINGUNO, GruposHistoricos.MINUTAL);";
		
		String tag = getTag(linea);
		String desc = getDesc(linea);
		
		res = res.replaceFirst("X_TAG_X", "\""+tag+"\"");
		res = res.replaceFirst("X_DESC_X", traduceDesc(desc));
		res = res.replaceFirst("X_GRUPO_X", getGrupo(getDesc(linea)));
		
		return res;
	}
}

package com.spring.cemstools.generacion;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneraCfgSist extends GeneraGeneral{

	
	public static String formateaString(List<String> info) {
		String res = "";
		boolean struct= false;
		
		for (String linea : info) {
			if(linea.contains(": Int;")) {
				if(struct) {
					res+="//**************************\n";
					res+="// CFG SIST\n";
					res+="//**************************\n\n";
					struct = true;
				}
				
				res+=stringToSistCfg(linea)+"\n\n";
			} 
		}
		
		return res;
	}
	
	//Genera fichero con el bloque addSenalHistCfgAnalogica generado a partir de un fichero bruto txt del PLC
		public static void generar(String origen, String destino) {
			boolean struct= false;
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(origen));
				BufferedWriter bw = new BufferedWriter(new FileWriter(destino));
				String linea;
				
				while((linea = br.readLine())!= null) {
						
					
					if(linea.contains(": Int;")) {
						if(struct) {
							bw.write("//**************************");
							bw.newLine();
							bw.write("// CFG SIST");
							bw.newLine();
							bw.write("//**************************");
							bw.newLine();
							bw.newLine();
							struct = true;
						}
						
						bw.write(stringToSistCfg(linea));
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
		private static String stringToSistCfg(String linea) {
			String res =  "X_TIPO_X(map, entorno,  \n"				  
					+"		prf, X_TAG_X, X_DESC_X, \n"
					+"		UNKNOWN, zona,\n"
					+"		entorno.getFormato0_00(), null,\n"
					+"		GruposOrganizacion.VERIFICACIONES, GrupoDibujado.NINGUNO, \n"
					+"		GruposHistoricos.MINUTAL);";
					
			String tag = getTag(linea);
			String desc = traduceDesc(getDesc(linea));
			String tipo = "addSenalHistCfgAnalogica";
			
			if(tag.contains("FECHA_") || tag.contains("FCH_")) {
				tipo = "addSenalHistCfgFecha";
			}

			res = res.replaceFirst("X_TAG_X", "\""+tag+"\"");
			res = res.replaceFirst("X_DESC_X", desc);
			res = res.replaceFirst("X_TIPO_X", tipo);
			
			return res;
		}


}



	

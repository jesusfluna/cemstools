package com.spring.cemstools.generacion;

import com.spring.cemstools.models.databaseForm;

public class BddScripts {
	private databaseForm database;

	public BddScripts(databaseForm database) {
		super();
		this.database = database;
		
		if(!this.database.getNombre().startsWith("BD") && !this.database.getNombre().startsWith("bd")) {
			this.database.setNombre("BD"+this.database.getNombre().toUpperCase().trim());
		}
	}

	public databaseForm getDatabase() {
		return database;
	}

	public void setDatabase(databaseForm database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return "BddScripts [database=" + database + "]";
	}
	
	public String getUsuariosServerSQL() {
		return "\n\n\n=================== Creacion de roles ========================\r\n\n\n"
				+ "CREATE ROLE \"SADMAOWNER\";\r\n"
				+ "ALTER ROLE \"SADMAOWNER\" WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION NOBYPASSRLS PASSWORD 'md5693d7d88b6a36e1644fdc02a587757fe' VALID UNTIL 'infinity';\r\n"
				+ "CREATE ROLE \"SADMA\";\r\n"
				+ "ALTER ROLE \"SADMA\" WITH SUPERUSER INHERIT CREATEROLE CREATEDB NOLOGIN REPLICATION NOBYPASSRLS VALID UNTIL 'infinity';\r\n"
				+ "GRANT \"SADMA\" TO \"SADMAOWNER\" WITH ADMIN OPTION GRANTED BY postgres;";
	}
	
	public String getBaseDatosSQL() {
		return "\n\n\n=================== Creacion BDD ========================\r\n\n\n"
				+ "CREATE DATABASE \""+this.database.getNombre()+"\" WITH TEMPLATE = template0 OWNER = \"SADMAOWNER\";"
				+ "\r\n"
				+ "REVOKE ALL ON DATABASE \""+this.database.getNombre()+"\" FROM PUBLIC;\r\n"
				+ "REVOKE ALL ON DATABASE \""+this.database.getNombre()+"\" FROM \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON DATABASE \""+this.database.getNombre()+"\" TO \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON DATABASE \""+this.database.getNombre()+"\" TO \"SADMA\" WITH GRANT OPTION;\r\n"
				+ "ALTER DATABASE \""+this.database.getNombre()+"\" SET search_path TO \"SADMAOWNER\";";
	}
	
	public String getEsquemasSQL() {
		return "\n\n\n=================== Creacion del esquema ========================\r\n\n\n"
				+ "CREATE SCHEMA \"SADMAOWNER\" AUTHORIZATION \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON SCHEMA \"SADMAOWNER\" TO \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON SCHEMA \"SADMAOWNER\" TO \"SADMA\" WITH GRANT OPTION;\r\n"
				+ "ALTER DEFAULT PRIVILEGES IN SCHEMA \"SADMAOWNER\" GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON TABLES TO \"SADMA\" WITH GRANT OPTION;\r\n"
				+ "\r\n";
	}
	
	public String getTableSpaces() {
		String res = "";
		res += "\n\n\n=================== Creacion de tablespaces ========================\r\n\n\n"
				+ "CREATE TABLESPACE \"D_SADMA_"+this.database.getId()+"\"\r\n"
				+ "  OWNER \"SADMAOWNER\"\r\n";
		
				if(this.getDatabase().isUnknown()) {
					res += "  LOCATION @UNKNOWN('C:\\"+this.database.getNombre()+"\\DATOS');"
							+ "\r\n"
							+ "CREATE TABLESPACE \"I_SADMA_"+this.database.getId()+"\"\r\n"
							+ "  OWNER \"SADMAOWNER\"\r\n"
							+ "  LOCATION @UNKNOWN('C:\\"+this.database.getNombre()+"\\INDICES');"
							+ "\r\n"
							+ "CREATE TABLESPACE \"U_SADMA_"+this.database.getId()+"\"\r\n"
							+ "  OWNER \"SADMAOWNER\"\r\n"
							+ "  LOCATION @UNKNOWN('c:\\"+this.database.getNombre()+"\\USUARIOS');\r\n"
							+ "\r\n";
				}else {
					res += "  LOCATION 'C:\\"+this.database.getNombre()+"\\DATOS';"
							+ "\r\n"
							+ "CREATE TABLESPACE \"I_SADMA_"+this.database.getId()+"\"\r\n"
							+ "  OWNER \"SADMAOWNER\"\r\n"
							+ "  LOCATION 'C:\\"+this.database.getNombre()+"\\INDICES';"
							+ "\r\n"
							+ "CREATE TABLESPACE \"U_SADMA_"+this.database.getId()+"\"\r\n"
							+ "  OWNER \"SADMAOWNER\"\r\n"
							+ "  LOCATION 'c:\\"+this.database.getNombre()+"\\USUARIOS';\r\n"
							+ "\r\n";
				}
				res += "REVOKE ALL ON TABLESPACE \"D_SADMA_"+this.database.getId()+"\" FROM PUBLIC;\r\n"
				+ "REVOKE ALL ON TABLESPACE \"D_SADMA_"+this.database.getId()+"\" FROM \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"D_SADMA_"+this.database.getId()+"\" TO \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"D_SADMA_"+this.database.getId()+"\" TO \"SADMA\" WITH GRANT OPTION;\r\n"
				+ "REVOKE ALL ON TABLESPACE \"I_SADMA_"+this.database.getId()+"\" FROM PUBLIC;\r\n"
				+ "REVOKE ALL ON TABLESPACE \"I_SADMA_"+this.database.getId()+"\" FROM \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"I_SADMA_"+this.database.getId()+"\" TO \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"I_SADMA_"+this.database.getId()+"\" TO \"SADMA\" WITH GRANT OPTION;\r\n"
				+ "REVOKE ALL ON TABLESPACE \"U_SADMA_"+this.database.getId()+"\" FROM PUBLIC;\r\n"
				+ "REVOKE ALL ON TABLESPACE \"U_SADMA_"+this.database.getId()+"\" FROM \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"U_SADMA_"+this.database.getId()+"\" TO \"SADMAOWNER\";\r\n"
				+ "GRANT ALL ON TABLESPACE \"U_SADMA_"+this.database.getId()+"\" TO \"SADMA\" WITH GRANT OPTION;";
				
				return res;
	}
	
	public String getTablasSQL() {
		String res =  "\n\n\n=================== Creacion de tablas ========================\r\n\n\n"
				+"SET default_tablespace = \"U_SADMA_"+this.database.getId()+"\";\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_h_ma_cfg (\r\n"
				+ "		id_central smallint NOT NULL,\r\n"
				+ "		id_estacion smallint NOT NULL,\r\n"
				+ "		id_sensor smallint NOT NULL,\r\n"
				+ "		id_cfg bigint NOT NULL,\r\n"
				+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
				+ "		ca_valor numeric(38,10),\r\n"
				+ "		fh_cambio timestamp without time zone\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_h_ma_cfg OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_h_ma_cfg_txt (\r\n"
				+ "    	id_central smallint NOT NULL,\r\n"
				+ "    	id_estacion smallint NOT NULL,\r\n"
				+ "    	id_sensor smallint NOT NULL,\r\n"
				+ "   	id_cfg bigint NOT NULL,\r\n"
				+ "   	fh_fecha timestamp without time zone NOT NULL,\r\n"
				+ "    	da_texto character varying(20),\r\n"
				+ "		fh_cambio timestamp without time zone\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_h_ma_cfg_txt OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "SET default_tablespace = \"D_SADMA_"+this.database.getId()+"\";\r\n"
				+ "\r\n";
		
		
				if(this.database.getNumEstaciones()==1) {
					res += "CREATE TABLE tb_h_ma_datosm (\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		ca_bruto numeric(12,4),\r\n"
							+ "		id_cbruto smallint,\r\n"
							+ "		ca_modificado numeric(12,4),\r\n"
							+ "		id_cmodificado smallint,\r\n"
							+ "		fh_cambio timestamp without time zone\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_datosm OWNER TO \"SADMAOWNER\";\r\n"
							+ "\r\n";
				}else{
					
					for(int i=1 ; i<= this.database.getNumEstaciones();i++) {
						res += "CREATE TABLE tb_h_ma_datosm_"+i+" (\r\n"
								+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
								+ "		id_central smallint NOT NULL,\r\n"
								+ "		id_estacion smallint NOT NULL,\r\n"
								+ "		id_sensor smallint NOT NULL,\r\n"
								+ "		ca_bruto numeric(12,4),\r\n"
								+ "		id_cbruto smallint,\r\n"
								+ "		ca_modificado numeric(12,4),\r\n"
								+ "		id_cmodificado smallint,\r\n"
								+ "		fh_cambio timestamp without time zone\r\n"
								+ ");\r\n"
								+ "ALTER TABLE tb_h_ma_datosm_"+i+" OWNER TO \"SADMAOWNER\";\r\n"
								+ "\r\n";
					}
				}
				res += "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_h_ma_verificaciones (\r\n"
				+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
				+ "		id_central smallint NOT NULL,\r\n"
				+ "		id_estacion smallint NOT NULL,\r\n"
				+ "		id_sensor smallint NOT NULL,\r\n"
				+ "		ca_duracion bigint,\r\n"
				+ "		id_cverificacion smallint NOT NULL,\r\n"
				+ "		id_cvalidacion smallint,\r\n"
				+ "		id_cresultado smallint,\r\n"
				+ "		ca_valor numeric(11,3),\r\n"
				+ "		fh_inyeccion timestamp without time zone,\r\n"
				+ "		fh_promedio timestamp without time zone,\r\n"
				+ "		fh_cambio timestamp without time zone\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_h_ma_verificaciones OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "SET default_tablespace = \"U_SADMA_"+this.database.getId()+"\";\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_centrales (\r\n"
				+ "		id_central smallint NOT NULL,\r\n"
				+ "		de_nombre character varying(20) NOT NULL,\r\n"
				+ "		cd_codigo character varying(20) NOT NULL,\r\n"
				+ "		nm_activo smallint DEFAULT 1 NOT NULL,\r\n"
				+ "		da_empresa character varying(20),\r\n"
				+ "		da_wgs84 character varying(20),\r\n"
				+ "		da_huso character varying(20),\r\n"
				+ "		da_utm character varying(20),\r\n"
				+ "		ca_altura real DEFAULT 0,\r\n"
				+ "		ca_diametro real DEFAULT 0,\r\n"
				+ "		da_direccion character varying(50)\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_centrales OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_cfg (\r\n"
				+ "		id_cfg bigint NOT NULL,\r\n"
				+ "		de_descripcion character varying(50) NOT NULL\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_cfg OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_cresultado (\r\n"
				+ "		de_descripcion character varying(50) NOT NULL,\r\n"
				+ "		id_cresultado smallint NOT NULL\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_cresultado OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_cvalidacion (\r\n"
				+ "		da_abreviatura character varying(10) NOT NULL,\r\n"
				+ "		de_descripcion character varying(50) NOT NULL,\r\n"
				+ "		id_cvalidacion smallint NOT NULL\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_cvalidacion OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_cverificacion (\r\n"
				+ "		de_descripcion character varying(50) NOT NULL,\r\n"
				+ "		id_cverificacion smallint NOT NULL\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_cverificacion OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_estaciones (\r\n"
				+ "		id_estacion smallint NOT NULL,\r\n"
				+ "		id_central smallint NOT NULL,\r\n"
				+ "		de_nombre character varying(50) NOT NULL,\r\n"
				+ "		cd_codigo character varying(20) NOT NULL,\r\n"
				+ "		nm_activo smallint DEFAULT 1 NOT NULL,\r\n"
				+ "		nm_red smallint NOT NULL,\r\n"
				+ "		nm_nodo smallint NOT NULL\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_estaciones OWNER TO \"SADMAOWNER\";\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "CREATE TABLE tb_p_ma_sensores (\r\n"
				+ "		id_sensor smallint NOT NULL,\r\n"
				+ "		id_estacion smallint NOT NULL,\r\n"
				+ "		id_central smallint NOT NULL,\r\n"
				+ "		da_descripcion character varying(20)\r\n"
				+ ");\r\n"
				+ "ALTER TABLE tb_p_ma_sensores OWNER TO \"SADMAOWNER\";";
				
				if(this.database.isLinealidad()) {
					res +="\r\n"
							+ "\r\n"
							+ "CREATE TABLE tb_h_ma_linealidad (\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		ca_duracion bigint,\r\n"
							+ "		id_clinealidad smallint NOT NULL,\r\n"
							+ "		id_cvalidacion smallint,\r\n"
							+ "		id_cresultado smallint,\r\n"
							+ "		ca_valor numeric(11,3),\r\n"
							+ "		id_ronda smallint NOT NULL DEFAULT 1,\r\n"
							+ "		fh_fecha2 timestamp without time zone,\r\n"
							+ "		fh_inyeccion timestamp without time zone,\r\n"
							+ "		fh_promedio timestamp without time zone,\r\n"
							+ "		fh_cambio timestamp without time zone\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_linealidad OWNER TO \"SADMAOWNER\";--\r\n"
							+ "\r\n"
							+ "\r\n"
							+ "CREATE TABLE tb_p_ma_clinealidad (\r\n"
							+ "		de_descripcion character varying(50) NOT NULL,\r\n"
							+ "		id_clinealidad smallint NOT NULL\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_p_ma_clinealidad OWNER TO \"SADMAOWNER\";\r\n"
							+ "\r\n";
					if(this.database.isReplicacion()) {
						res +="\r\n"
								+ "\r\n"
								+ "CREATE TABLE tb_h_ma_lineal_cambios (\r\n"
								+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
								+ "		id_central smallint NOT NULL,\r\n"
								+ "		id_estacion smallint NOT NULL,\r\n"
								+ "		id_sensor smallint NOT NULL,\r\n"
								+ "		ca_duracion bigint,\r\n"
								+ "		id_clinealidad smallint NOT NULL,\r\n"
								+ "		id_cvalidacion smallint,\r\n"
								+ "		id_cresultado smallint,\r\n"
								+ "		ca_valor numeric(11,3),\r\n"
								+ "		id_ronda smallint NOT NULL DEFAULT 1,\r\n"
								+ "		fh_fecha2 timestamp without time zone,\r\n"
								+ "		fh_inyeccion timestamp without time zone,\r\n"
								+ "		fh_promedio timestamp without time zone,\r\n"
								+ "		fh_cambio timestamp without time zone NOT NULL,\r\n"
								+ "		ca_flags int,\r\n"
								+ "		id_cambio int NOT NULL,\r\n"
								+ "		id_seleccionado smallint\r\n"
								+ ");\r\n"
								+ "ALTER TABLE tb_h_ma_lineal_cambios OWNER TO \"SADMAOWNER\";\r\n"
								+ "\r\n";
					}
				}
				
				if(this.database.isSustitucion() && this.database.getNumEstaciones() ==1) {
					
					res +="CREATE TABLE tb_h_ma_datosm_sust (\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		data bytea\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_datosm_sust OWNER TO \"SADMAOWNER\";\r\n";
					
				}else if(this.database.isSustitucion() && this.database.getNumEstaciones() >1) {
					
					for(int i = 1; i<=this.database.getNumEstaciones(); i++) {
						res +=  "CREATE TABLE tb_h_ma_datosm_"+i+"_sust (\r\n"
								+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
								+ "		id_central smallint NOT NULL,\r\n"
								+ "		id_estacion smallint NOT NULL,\r\n"
								+ "		id_sensor smallint NOT NULL,---da_sensor character varying(5) NOT NULL,\r\n"
								+ "		data bytea\r\n"
								+ ");\r\n"
								+ "ALTER TABLE tb_h_ma_datosm_"+i+"_sust OWNER TO \"SADMAOWNER\";\r\n";
					}
				}
				
				
				if(this.database.isHorarios() && this.database.getNumEstaciones() ==1) {
					
					res +="CREATE TABLE tb_h_ma_datosh(\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		ca_valor numeric(12, 4) NOT NULL,\r\n"
							+ "		id_cvalidacion smallint NOT NULL\r\n"
							+ "	);\r\n"
							+ "ALTER TABLE tb_h_ma_datosh OWNER TO \"SADMAOWNER\";\r\n";
					
				}else if(this.database.isHorarios() && this.database.getNumEstaciones() >1) {
					
					for(int i = 1; i<=this.database.getNumEstaciones(); i++) {
						res +="CREATE TABLE tb_h_ma_datosh_"+i+"(\r\n"
								+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
								+ "		id_central smallint NOT NULL,\r\n"
								+ "		id_estacion smallint NOT NULL,\r\n"
								+ "		id_sensor smallint NOT NULL,\r\n"
								+ "		ca_valor numeric(12, 4) NOT NULL,\r\n"
								+ "		id_cvalidacion smallint NOT NULL\r\n"
								+ "	);\r\n"
								+ "	\r\n"
								+ "ALTER TABLE tb_h_ma_datosh_"+i+" OWNER TO \"SADMAOWNER\";\r\n";
					}
				}
				
				
				if(this.database.isReplicacion()) {
					res +=  "CREATE TABLE tb_h_ma_cfg_cambios (\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		id_cfg bigint NOT NULL,\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		ca_valor numeric(38,10),\r\n"
							+ "		fh_cambio timestamp without time zone,\r\n"
							+ "		id_cambio int NOT NULL,\r\n"
							+ "		id_seleccionado smallint\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_cfg_cambios OWNER TO \"SADMAOWNER\";--\r\n"
							+ "\r\n"
							+ "\r\n"
							+ "CREATE TABLE tb_h_ma_cfg_txt_cambios (\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		id_cfg bigint NOT NULL,\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		da_texto character varying(20),\r\n"
							+ "		fh_cambio timestamp without time zone,\r\n"
							+ "		id_cambio int NOT NULL,\r\n"
							+ "		id_seleccionado smallint\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_cfg_txt_cambios OWNER TO \"SADMAOWNER\";--\r\n"
							+ "\r\n"
							+ "\r\n"
							+ "CREATE TABLE tb_h_ma_verif_cambios (\r\n"
							+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
							+ "		id_central smallint NOT NULL,\r\n"
							+ "		id_estacion smallint NOT NULL,\r\n"
							+ "		id_sensor smallint NOT NULL,\r\n"
							+ "		ca_duracion bigint,\r\n"
							+ "		id_cverificacion smallint NOT NULL,\r\n"
							+ "		id_cvalidacion smallint,\r\n"
							+ "		id_cresultado smallint,\r\n"
							+ "		ca_valor numeric(11,3),\r\n"
							+ "		fh_inyeccion timestamp without time zone,\r\n"
							+ "		fh_promedio timestamp without time zone,\r\n"
							+ "		fh_cambio timestamp without time zone NOT NULL,\r\n"
							+ "		ca_flags int,\r\n"
							+ "		id_cambio int NOT NULL,\r\n"
							+ "		id_seleccionado smallint\r\n"
							+ ");\r\n"
							+ "ALTER TABLE tb_h_ma_verif_cambios OWNER TO \"SADMAOWNER\";\r\n";
					
					if(this.database.getNumEstaciones() == 1) {
						res +="CREATE TABLE tb_h_ma_datosm_cambios (\r\n"
								+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
								+ "		id_central smallint NOT NULL,\r\n"
								+ "		id_estacion smallint NOT NULL,\r\n"
								+ "		id_sensor smallint NOT NULL,\r\n"
								+ "		ca_bruto numeric(12,4),\r\n"
								+ "		id_cbruto smallint,\r\n"
								+ "		ca_modificado numeric(12,4),\r\n"
								+ "		id_cmodificado smallint,\r\n"
								+ "		fh_cambio timestamp without time zone NOT NULL,\r\n"
								+ "		ca_flags int,\r\n"
								+ "		id_cambio int NOT NULL,\r\n"
								+ "		id_seleccionado smallint \r\n"
								+ ");\r\n"
								+ "ALTER TABLE tb_h_ma_datosm_cambios OWNER TO \"SADMAOWNER\";\r\n";
					}else{
						for(int i=1; i<=this.database.getNumEstaciones();i++) {
							res +="CREATE TABLE tb_h_ma_datosm_"+i+"_cambios (\r\n"
									+ "		fh_fecha timestamp without time zone NOT NULL,\r\n"
									+ "		id_central smallint NOT NULL,\r\n"
									+ "		id_estacion smallint NOT NULL,\r\n"
									+ "		id_sensor smallint NOT NULL,\r\n"
									+ "		ca_bruto numeric(12,4),\r\n"
									+ "		id_cbruto smallint,\r\n"
									+ "		ca_modificado numeric(12,4),\r\n"
									+ "		id_cmodificado smallint,\r\n"
									+ "		fh_cambio timestamp without time zone NOT NULL,\r\n"
									+ "		ca_flags int,\r\n"
									+ "		id_cambio int NOT NULL,\r\n"
									+ "		id_seleccionado smallint \r\n"
									+ ");\r\n"
									+ "ALTER TABLE tb_h_ma_datosm_"+i+"_cambios OWNER TO \"SADMAOWNER\";\r\n";
						}
					}
				}
				
				return res;
	}
	
	public String getIndicesSQL() {
		String res = "\n\n\n=================== Creacion de indices ========================\r\n\n\n"
				+"SET default_tablespace = \"I_SADMA_"+this.getDatabase().getId()+"\";\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_CFG00\" PRIMARY KEY (id_central, id_estacion, id_sensor, id_cfg, fh_fecha);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg_txt\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_CFG_TXT00\" PRIMARY KEY (id_central, id_estacion, id_sensor, id_cfg, fh_fecha);\r\n"
				+ "\r\n";
				
				if(this.getDatabase().getNumEstaciones()==1) {
					res += "ALTER TABLE ONLY tb_h_ma_datosm\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM_00\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central);\r\n"
							+ "	\r\n";
				}else {
					
					for(int i=1; i<= this.getDatabase().getNumEstaciones();i++) {
						res += "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM_0"+i+"\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central);\r\n"
								+ " \r\n";
					}
				}

				
				res += "ALTER TABLE ONLY tb_h_ma_verificaciones\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_VERIFICACIONES10\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, id_cverificacion);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_centrales\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CENTRALES00\" PRIMARY KEY (id_central);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_cfg\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CFG00\" PRIMARY KEY (id_cfg);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_cresultado\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CRESULTADO00\" PRIMARY KEY (id_cresultado);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_cvalidacion\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CVALIDACION00\" PRIMARY KEY (id_cvalidacion);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_cverificacion\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CVERIFICACION00\" PRIMARY KEY (id_cverificacion);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_estaciones\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_ESTACIONES00\" PRIMARY KEY (id_estacion, id_central);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_sensores\r\n"
				+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_SENSORES00\" PRIMARY KEY (id_sensor, id_estacion, id_central);\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_CFG01\" FOREIGN KEY (id_cfg) REFERENCES tb_p_ma_cfg(id_cfg) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_CFG02\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg_txt\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_CFG_TXT01\" FOREIGN KEY (id_cfg) REFERENCES tb_p_ma_cfg(id_cfg) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_cfg_txt\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_CFG_TXT02\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
				+ "\r\n";
				
				if(this.getDatabase().getNumEstaciones()==1) {
					res+= "ALTER TABLE ONLY tb_h_ma_datosm\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM_00\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_datosm\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM_02\" FOREIGN KEY (id_cbruto) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
							+ "\r\n";
					
					if(this.getDatabase().isReplicacion()) {
						res += "ALTER TABLE ONLY tb_h_ma_datosm_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM_C_00\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, fh_cambio, id_cambio);\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_datosm_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM_C_02\" FOREIGN KEY (id_cbruto) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
								+ "\r\n";
					}
					
					if(this.getDatabase().isSustitucion()) {
						res += "ALTER TABLE ONLY tb_h_ma_datosm_sust\r\n"
								+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM_S_00\" PRIMARY KEY (fh_fecha, id_central, id_estacion, id_sensor);---da_sensor \r\nALTER TABLE ONLY tb_h_ma_datosm_1_sust\r\n"
								+ "	\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM_S_01\" FOREIGN KEY (id_central,id_estacion) REFERENCES tb_p_ma_estaciones(id_central,id_estacion) ON DELETE CASCADE;\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_datosm_1_sust\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM_S_02\" FOREIGN KEY (id_central,id_estacion,id_sensor) REFERENCES tb_p_ma_sensores(id_central,id_estacion,id_sensor) ON DELETE CASCADE;\r\n"
								+ "\r\n";
					}
					
				}else {
					
					for(int i=1; i<= this.getDatabase().getNumEstaciones();i++) {
						res+= "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM"+i+"_00\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM"+i+"_02\" FOREIGN KEY (id_cbruto) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
								+ "\r\n";
						
						if(this.getDatabase().isReplicacion()) {
							res += "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"_cambios\r\n"
									+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM"+i+"_C_00\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, fh_cambio, id_cambio);\r\n"
									+ "\r\n"
									+ "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"_cambios\r\n"
									+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM"+i+"_C_02\" FOREIGN KEY (id_cbruto) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
									+ "\r\n";
					}
						
						if(this.getDatabase().isSustitucion()) {
							res += "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"_sust\r\n"
									+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_DATOSM"+i+"_S_00\" PRIMARY KEY (fh_fecha, id_central, id_estacion, id_sensor);\r\n"
									+ "\r\n"
									+ "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"_sust\r\n"
									+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM"+i+"_S_01\" FOREIGN KEY (id_central,id_estacion) REFERENCES tb_p_ma_estaciones(id_central,id_estacion) ON DELETE CASCADE;\r\n"
									+ "\r\n"
									+ "ALTER TABLE ONLY tb_h_ma_datosm_"+i+"_sust\r\n"
									+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSM"+i+"_S_02\" FOREIGN KEY (id_central,id_estacion,id_sensor) REFERENCES tb_p_ma_sensores(id_central,id_estacion,id_sensor) ON DELETE CASCADE;\r\n"
									+ "\r\n";
						}
					}
				}
				
				res += "ALTER TABLE ONLY tb_h_ma_verificaciones\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES11\" FOREIGN KEY (id_cvalidacion) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_verificaciones\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES12\" FOREIGN KEY (id_cverificacion) REFERENCES tb_p_ma_cverificacion(id_cverificacion) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_h_ma_verificaciones\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES13\" FOREIGN KEY (id_cresultado) REFERENCES tb_p_ma_cresultado(id_cresultado) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_estaciones\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_P_MA_ESTACIONES01\" FOREIGN KEY (id_central) REFERENCES tb_p_ma_centrales(id_central) ON DELETE CASCADE;\r\n"
				+ "\r\n"
				+ "ALTER TABLE ONLY tb_p_ma_sensores\r\n"
				+ "    ADD CONSTRAINT \"I_FK_TB_P_MA_SENSORES04\" FOREIGN KEY (id_estacion, id_central) REFERENCES tb_p_ma_estaciones(id_estacion, id_central) ON DELETE CASCADE;\r\n"
				+ "\r\n";
				
				if(this.getDatabase().isLinealidad()) {
					res +="ALTER TABLE ONLY tb_h_ma_linealidad\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_LINEALIDAD10\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, id_clinealidad, id_ronda);\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_p_ma_clinealidad\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_P_MA_CLINEALIDAD00\" PRIMARY KEY (id_clinealidad);\r\n"
							+ "\r\n";
					if(this.getDatabase().isReplicacion()) {
						res +="ALTER TABLE ONLY tb_h_ma_lineal_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_LINEALIDAD_C_10\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, id_clinealidad, id_ronda, fh_cambio, id_cambio);\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_lineal_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_LINEALIDAD_C_11\" FOREIGN KEY (id_cvalidacion) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_lineal_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_LINEALIDAD_C_12\" FOREIGN KEY (id_clinealidad) REFERENCES tb_p_ma_clinealidad(id_clinealidad) ON DELETE CASCADE;\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_lineal_cambios\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_LINEALIDAD_C_13\" FOREIGN KEY (id_cresultado) REFERENCES tb_p_ma_cresultado(id_cresultado) ON DELETE CASCADE;\r\n"
								+ "\r\n";
					}
				
				}
				
				if(this.getDatabase().isHorarios() && this.getDatabase().getNumEstaciones()==1) {
					res +="ALTER TABLE ONLY tb_h_ma_datosh\r\n"
							+ "    ADD CONSTRAINT \"PK_tb_h_ma_datosh\" PRIMARY KEY (id_central, id_estacion, id_sensor, fh_fecha);\r\n"
							+ "	\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_datosh\r\n"
							+ "    ADD CONSTRAINT \"FK_tb_h_ma_datosh_tb_p_ma_cvalidacion\" FOREIGN KEY (id_cvalidacion) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_datosh\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSH_00\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
							+ "\r\n";
					
				}else {
					for(int i=1; i<this.getDatabase().getNumEstaciones();i++) {
						res += "ALTER TABLE ONLY tb_h_ma_datosh_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"PK_tb_h_ma_datosh"+i+"\" PRIMARY KEY (id_central, id_estacion, id_sensor, fh_fecha);\r\n"
								+ "	\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_datosh_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"FK_tb_h_ma_datosh_"+i+"_tb_p_ma_cvalidacion\" FOREIGN KEY (id_cvalidacion) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
								+ "\r\n"
								+ "ALTER TABLE ONLY tb_h_ma_datosh_"+i+"\r\n"
								+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_DATOSH_"+i+"_00\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
								+ "\r\n";
					}
				}
				
				
				if(this.getDatabase().isReplicacion()) {
					res	+=	"ALTER TABLE ONLY tb_h_ma_cfg_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_CFG_C_00\" PRIMARY KEY (id_central, id_estacion, id_sensor, id_cfg, fh_fecha, fh_cambio, id_cambio);\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_cfg_txt_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_CFG_TXT_C_00\" PRIMARY KEY (id_central, id_estacion, id_sensor, id_cfg, fh_fecha, fh_cambio, id_cambio);\r\n"
							+ "\r\n"
							+"ALTER TABLE ONLY tb_h_ma_verif_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_PK_TB_H_MA_VERIFICACIONES_C_10\" PRIMARY KEY (fh_fecha, id_sensor, id_estacion, id_central, id_cverificacion, fh_cambio, id_cambio);\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_verif_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES_C_11\" FOREIGN KEY (id_cvalidacion) REFERENCES tb_p_ma_cvalidacion(id_cvalidacion) ON DELETE CASCADE;\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_verif_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES_C_12\" FOREIGN KEY (id_cverificacion) REFERENCES tb_p_ma_cverificacion(id_cverificacion) ON DELETE CASCADE;\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_verif_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_VERIFICACIONES_C_13\" FOREIGN KEY (id_cresultado) REFERENCES tb_p_ma_cresultado(id_cresultado) ON DELETE CASCADE;\r\n"
							+ "\r\n"
							+ "ALTER TABLE ONLY tb_h_ma_lineal_cambios\r\n"
							+ "    ADD CONSTRAINT \"I_FK_TB_H_MA_LINEALIDAD_C_10\" FOREIGN KEY (id_sensor, id_estacion, id_central) REFERENCES tb_p_ma_sensores(id_sensor, id_estacion, id_central) ON DELETE CASCADE;\r\n"
							+ "\r\n";
				}
		
		return res;
	}
	
	public String getInsertsSQL(){
		String res = "\n\n\n=================== Creacion de Inserts ========================\r\n\n\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_centrales (id_central, de_nombre, cd_codigo, nm_activo, da_empresa, da_wgs84, da_huso, da_utm, ca_altura, ca_diametro, da_direccion) VALUES ("+this.getDatabase().getId()+", '"+this.getDatabase().getNombre()+"', '1', 1, '"+this.getDatabase().getNombre()+"', 'wgs84', 'zona huso', 'coordenadas utm', 20, 6, 'dirección');\r\n"
				+ "\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000050, 'PCI en fórmula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000040, '%O en formula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000030, '%N en fórmula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000020, '%S en fórmula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000010, '%C en fórmula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000000, '%H en fórmula, cond. normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000070, 'CGV en fórmula, normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000071, 'CGV en fórmula, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000080, 'CE en fórmula, normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000081, 'CE en fórmula, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000090, 'Factor-F en fórmula, normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000091, 'Factor-F fórmula, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000100, 'TMp en fórmula, normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000101, 'TMp fórmula, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000021, '%S en fórmula, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000110, 'Diametro chimenea, normal');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000111, 'Diametro chimenea, secundario');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1, 'Valor por defecto a usar GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1400, 'Límite máximo GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1401, 'Límite máximo GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1211, 'Plena Carga GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1201, 'Minimo Tecnico GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (20, 'Valores sustitivos Bypass GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (21, 'Valores sustitivos Bypass GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (50, 'Limite de detecccion');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1050, 'Fecha de validacion GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1051, 'Fecha de validacion Otro combustible');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1210, 'Plena Carga');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1410, 'Limite t/h Gas');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1411, 'Limite t/h Otro');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500, 'Valor mínimo rango');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1510, 'Valor máximo rango');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (2000, 'Valor bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (2001, 'Valor medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (2002, 'Valor alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (2010, 'Ajuste Bias');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (3000, 'SO2. monitoreo de caldera');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (3001, 'CO2. monitoreo de caldera');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000120, 'Densidad gas natural ');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000121, 'Densidad Fuel oil ');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001000, 'Mínimo valor de presión de verificación');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001010, 'Máximo valor de presión de verificación');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001020, 'Mínimo valor de presión (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001030, 'Máxima valor de presión (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001040, 'Presión alta de verificación');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001050, 'Presión baja de verificación');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001060, 'Histéresis de verificación');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001070, 'Nivel bajo (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001080, 'Nivel muy bajo (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001090, 'Histéresis (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001110, 'Valor de referencia de verificación cero');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001120, 'Dif. máxima para no dar aviso verif. cero');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001130, 'Dif. máxima para no dar error verif. cero');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001140, 'Valor de referencia de verificación span');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001150, 'Dif. máxima para no dar aviso verif. span');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001160, 'Dif. máxima para no dar error verif. span');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001170, 'Orden  de verificación (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001180, 'Tiempo de verificación de cero (segundos)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001190, 'Tiempo de verificación de SPAN (segundos)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001200, 'Segundos desde las 0:0 de fecha ref. verif.');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001210, 'Periocidad (días) de verif. analiz.(0=no, 32=mes)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001220, 'Días desde 1/1/2000 de fecha refer. verif.');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001230, 'Periocidad (horas) de verif. (<=0 no, 32767=mes)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001240, 'Fecha de ref. soplado de sonda (seg desde 00:00)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001250, 'Periocidad (horas) de soplado de sonda');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001260, 'Tiempo no represent. analizador (segundos)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001270, 'Duración retrosoplado sonda (segundos)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001280, 'Tiempo represent. verificación (segundos)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001205, 'Segundos desde las 0:0 de fecha ref. verif.Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001215, 'Periocidad (días) de verif(0=no, 32=mes).Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001225, 'Días desde 1/1/2000 de fecha refer. verif. Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001235, 'Periocidad (H) de verif.(<=0 no,32767=mes).Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001245, 'Fecha de ref. soplado sonda(seg desde 0:0).Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001255, 'Periocidad (horas) de soplado de sonda. Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001265, 'Tiempo no represen. analizador (sgs). Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001275, 'Duración retrosoplado sonda (sgs). Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001285, 'Tiempo represent. verificación (sgs).Parada');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001290, 'Valor de referencia de verificación span bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001300, 'Dif. máxima para no dar aviso verif. span bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001310, 'Dif. máxima para no dar error verif. span bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001320, 'Tiempo de verificación de cero (min) (botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001330, 'Tiempo de verificación de span (min) (botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001340, 'Incertidumbre verificación cero (botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001350, 'Fecha de caducidad de botella (solo con días)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001360, 'Incertidumbre verificación valor bajo (botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001370, 'Incertidumbre verificación span (botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001400, 'Valor linealidad nivel bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001410, 'Valor linealidad nivel medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001420, 'Valor linealidad nivel alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001430, 'Incertidumbre linealidad nivel bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001440, 'Incertidumbre linealidad nivel medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001450, 'Incertidumbre linealidad nivel alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001460, 'Dif. maxima error linealidad nivel bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001470, 'Dif. maxima error linealidad nivel medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001510, 'Peso molecular de los gases');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001520, 'Fecha caducidad (dias) linealidad nivel bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001530, 'Fecha caducidad (dias) linealidad nivel medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001540, 'Fecha caducidad (dias) linealidad nivel alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000001, 'TB_H_MA_BOTELLAS Numero ');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000002, 'TB_H_MA_BOTELLAS Resto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000003, 'TB_H_MA_BOTELLAS Certificado');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000011, 'TB_H_MA_BOTELLAS Numero Linealidad baja');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000012, 'TB_H_MA_BOTELLAS Resto Linealidad baja');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000013, 'TB_H_MA_BOTELLAS Certificado Linealidad baja');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000021, 'TB_H_MA_BOTELLAS Numero Linealidad media');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000022, 'TB_H_MA_BOTELLAS Resto Linealidad media');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000023, 'TB_H_MA_BOTELLAS Certificado Linealidad media');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000031, 'TB_H_MA_BOTELLAS Numero Linealidad alta');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000032, 'TB_H_MA_BOTELLAS Resto Linealidad alta');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000033, 'TB_H_MA_BOTELLAS Certificado Linealidad alta');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (0, 'Valor por defecto a usar');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (10, 'Recta GN LIN(0)POL(1)LOG(2)EXP(3)POT(4)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (11, 'Recta GO LIN(0)POL(1)LOG(2)EXP(3)POT(4)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000, 'B0 con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1001, 'B0 con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1010, 'B1 con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1011, 'B1 con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1020, 'B2 con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1021, 'B2 con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1030, 'Correlacion con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1031, 'Correlacion con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1040, 'Min Max Potencial y Fecha Validacion GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1041, 'Min Max Potencial y Fecha Validacion DIESEL');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1100, 'Rango minimo aplicacion recta con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1101, 'Rango minimo aplicacion recta con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1110, 'Rango maximo aplicacion recta con GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1111, 'Rango maximo aplicacion recta con GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1200, 'Ejemplo sensor de potencia, mínimo técnico');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1300, 'Factor escala maximo / minimo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000061, 'Constante Caudal DIESEL');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000060, 'Constante Caudal GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001480, 'Dif. maxima error linealidad nivel alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001351, 'Fecha de caducidad de botella (solo con días) bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001352, 'Fecha de caducidad de botella (solo con días) Span');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001550, 'Tiempo inyeccion gas linealialidad baja min');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001560, 'Tiempo inyeccion gas linealialidad media min');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001570, 'Tiempo inyeccion gas linealialidad alta min');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000004, 'BOTELLAS Numero bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000005, 'BOTELLAS Resto bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000006, 'BOTELLAS Certificado bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000007, 'BOTELLAS Numero alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000008, 'BOTELLAS Resto alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000009, 'BOTELLAS Certificado alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001600, 'Valvula botella Cero');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001610, 'Valvula botella bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001620, 'Valvula botella Span');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000130, 'Factor de emision MP AP-42 GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000000131, 'Factor de emision MP AP-42 GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000100, 'Valor impuesto equivalente');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000101, 'Número de serie');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000103, 'Modelo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000104, 'Res. Ex. Ultima validacion CEMS');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000108, 'Origen señal patrón referencia');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000109, 'Nombre UGE');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000115, 'Nombre central');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000110, 'Valor costo contaminación per cápita');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000120, 'Valor coeficiente calidad del aire');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000130, 'Valor población de la comuna');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000140, 'Valor contaminantes globales (Tasa U.)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500000150, 'Nivel Alto (para botella)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001705, 'Duracion espera (min) (unidad detenida)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001700, 'Duracion espera (min) ');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001710, 'Duracion Cero (min)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001715, 'Duracion Cero (min) (Unidad detenida)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001720, 'Duracion Span (min) ');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001730, 'Duracion soplado extractivo (min)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001735, 'Duracion soplado extractivo unidad detenida (min)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001740, 'Duracion espera span (min)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000001271, 'Duración sistema extractivo (min)');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (60, 'Origen de datos (0=N, 1=P, 2=R, 3=Med, 4=P/R, 5=R/');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001110, 'Linealidad aviso bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001120, 'Linealidad Alarma bajo');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001130, 'Linealidad aviso medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001140, 'Linealidad Alarma medio');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001150, 'Linealidad aviso Alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1500001160, 'Linealidad Alarma Alto');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1420, 'Límite porcentaje máximo GN');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1421, 'Límite porcentaje máximo GO');\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cfg (id_cfg, de_descripcion) VALUES (1000002000, 'Oxigeno de referencia');\r\n"
				+ "\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('OK', 0);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Detenido por usuario', 1);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo compresor', 2);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo condensados', 3);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo calentamiento línea', 4);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo analizador', 5);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Bajo caudal', 6);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo bomba', 7);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo alimentación', 8);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo calentamiento sonda', 9);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo filtro', 10);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Mantenimiento', 11);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Tiempo excedido', 12);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('No realizado', 15);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Fallo reconocimiento estado', 16);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cresultado (de_descripcion, id_cresultado) VALUES ('Invalidado', 13);\r\n"
				+ "\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('NA', 'No aplica', 29);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('T', 'Temporal', 0);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('V', 'Validado', 1);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('O', 'Corregido', 2);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('R', 'Reconstruido', 3);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('C', 'Perturbado por calibracion', 4);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('Z', 'Perturbado por chequeo cero', 5);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('M', 'Perturbado por mantenimiento', 6);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('D', 'Erroneo por fallo tecnico', 7);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('N', 'Erroneo por razon desconocida', 8);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('P', 'Parada', 9);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('_', 'No asignado aun', 10);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('A', 'Prevalidado automaticamente', 11);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('F', 'Verificacion', 12);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('E', 'Fallo Ajeno', 13);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('NE', 'No existe', 14);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('MMC', 'Midienendo condicionalmente', 15);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CA', 'Calibracion sensor', 16);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CCDC', 'Calibracion cero desviacion calibracion', 17);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CCEL', 'Calibracion cero error linealidad', 18);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CM', 'Calibracion media', 19);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CSDC', 'Calibracion span desviacion calibracion', 20);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('CSEL', 'Calibracion span error linealidad', 21);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('ER', 'Exactitud relativa', 22);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('MEC', 'Margen de error cero', 23);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('MES', 'Margen de error span', 24);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('EC', 'Ensayo de correlacion', 25);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('ACA', 'Auditoria de correlacion absoluta', 26);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('ACR', 'Auditoria de correlacion de respuesta', 27);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cvalidacion (da_abreviatura, de_descripcion, id_cvalidacion) VALUES ('ARR', 'Auditoria de respuesta relativa', 28);\r\n"
				+ "\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cverificacion VALUES ('Cero', 0);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cverificacion VALUES ('Bajo', 1);\r\n"
				+ "INSERT INTO \"SADMAOWNER\".tb_p_ma_cverificacion VALUES ('Alto', 2);\r\n"
				+ "\r\n";
				
		if(this.getDatabase().getNumEstaciones()==1) {
			
			if(this.getDatabase().isUnknown()) {
				res += "INSERT INTO \"SADMAOWNER\".tb_p_ma_estaciones (id_estacion, id_central, de_nombre, cd_codigo, nm_activo, nm_red, nm_nodo) VALUES (1, "+this.getDatabase().getId()+", @UNKNOWN('"+this.getDatabase().getNombre()+"'), '1', 1, 1, 1);\r\n";
			}else {
				res += "INSERT INTO \"SADMAOWNER\".tb_p_ma_estaciones (id_estacion, id_central, de_nombre, cd_codigo, nm_activo, nm_red, nm_nodo) VALUES (1, "+this.getDatabase().getId()+", '"+this.getDatabase().getNombre().replaceAll("BD", "")+"', '1', 1, 1, 1);\r\n";
			}
			
		}else {
			if(this.getDatabase().isUnknown()) {
				for(int i=1;i<=this.getDatabase().getNumEstaciones();i++) {
					res += "INSERT INTO \"SADMAOWNER\".tb_p_ma_estaciones (id_estacion, id_central, de_nombre, cd_codigo, nm_activo, nm_red, nm_nodo) VALUES ("+i+", "+this.getDatabase().getId()+", '"+this.getDatabase().getNombre()+"_"+i+"', '1', 1, 1, 1);\r\n";
				}
			}else {
				for(int i=1;i<=this.getDatabase().getNumEstaciones();i++) {
					res += "INSERT INTO \"SADMAOWNER\".tb_p_ma_estaciones (id_estacion, id_central, de_nombre, cd_codigo, nm_activo, nm_red, nm_nodo) VALUES ("+i+", "+this.getDatabase().getId()+",  @UNKNOWN('"+this.getDatabase().getNombre().replaceAll("BD", "")+"_"+i+"'), '1', 1, 1, 1);\r\n";
				}
			}
		}
		
		return res;
	}
	
	public String getAllSQL() {
		String res = "";
		
		res += this.getUsuariosServerSQL();
		res += this.getBaseDatosSQL();
		res += this.getEsquemasSQL();
		res += this.getTableSpaces();
		res += this.getTablasSQL();
		res += this.getIndicesSQL();
		res += this.getInsertsSQL();
		
		return res;
	}
}

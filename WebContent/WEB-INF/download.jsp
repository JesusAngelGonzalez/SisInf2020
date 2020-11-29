<%
	String filename= (String) request.getAttribute("fichero");

	response.setContentType("APPLICATION/OCTET-STREAM");
	response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
	java.io.FileInputStream fis = new java.io.FileInputStream(filename);
	
	int i;
	while((i=fis.read()) != -1){
		out.write(i);
	}
	fis.close();
	
	java.io.File zip = new java.io.File(filename);
	zip.delete();
%>
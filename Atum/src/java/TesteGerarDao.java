
import gerador.bean.Atributos;
import gerador.bean.Classe;
import gerador.dao.DAO;
import gerador.utils.Conexao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.hibernate.Session;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author GCI
 */
public class TesteGerarDao {

    public static void main(String[] args) {
        Session session = Conexao.getConexao();
        DAO dao = new DAO(session);
        Classe classe = (Classe) dao.busca(Classe.class, 1);
        File diretorio = new File("C:\\Users\\GCI\\Documents\\NetBeansProjects\\rborges~subversion\\rborges~subversion\\TesteRodrigo&Ian\\src\\java\\gerador");
        boolean statusDiretorio = diretorio.isDirectory();
        System.out.println(statusDiretorio);
        File arquivo = new File(diretorio, classe.getNomeClasse() + "DAO.java");
        try {
            boolean statusArq = arquivo.createNewFile();
            System.out.println(statusArq);
            System.out.println("Comecando a escrever no arquivo");
            FileReader fileR = new FileReader(arquivo);
            BufferedReader buffR = new BufferedReader(fileR);
            FileWriter fileW = new FileWriter(arquivo);
            BufferedWriter buffW = new BufferedWriter(fileW);
            buffW.write("package gerador;");
            buffW.newLine();
            buffW.write(geraImports());
            buffW.newLine();
            buffW.write(classe.getModerador().getDescricao() + " class " + classe.getNomeClasse() + "DAO implements Serializable{");
            buffW.newLine();
            buffW.write("private static final long serialVersionUID = 1L;");
            buffW.newLine();
            buffW.write("protected Connection connection;");
            buffW.newLine();
            //Cria o contrutor da classe
            buffW.write("public " + classe.getNomeClasse() + "DAO(Connection connection){");
            buffW.write("this.connection = connection;");
            buffW.write("}");
            buffW.newLine();
            //Criar o metodo inserir
            buffW.write("public void inserir(" + classe.getNomeClasse() + " " + classe.getNomeClasse().toLowerCase() + "){");
            buffW.write("PreparedStatement pstmt;");
            buffW.newLine();
            String caracteres = "";
            for (int i = 0; i < classe.getAtributos().size(); i++) {
                if (i == classe.getAtributos().size() - 1) {
                    caracteres += "?";
                } else {
                    caracteres += "?,";
                }
            }
            buffW.write("StringBuilder sql = new StringBuilder();");
            buffW.write("sql.append(\"INSERT INTO " + classe.getTabela() + " VALUES (" + caracteres + ")\");");
            buffW.newLine();
            buffW.write("try {");
            buffW.newLine();
            buffW.write("pstmt = connection.prepareStatement(sql.toString());");
            buffW.newLine();
            int count = 1;
            for (Atributos item : classe.getAtributos()) {
                buffW.write(retornaTipoPstmt(item.getTipo().getDescricao()) + "(" + count + "," + classe.getNomeClasse().toLowerCase() + ".get" + item.getNome().substring(0, 1).toUpperCase().concat(item.getNome().substring(1)) + "());");
                buffW.newLine();
                count++;
            }
            buffW.write("pstmt.execute();");
            buffW.newLine();
            buffW.write("} catch (SQLException ex) {");
            buffW.newLine();
            buffW.write(" Logger.getLogger(" + classe.getNomeClasse() + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
            buffW.newLine();
            buffW.write("}");
            buffW.newLine();
            buffW.write("}");
            buffW.newLine();
            buffW.write("public List<" + classe.getNomeClasse() + "> find() {");
            buffW.newLine();
            buffW.write("PreparedStatement pstmt;");
            buffW.newLine();
            buffW.write("ResultSet rs;");
            buffW.newLine();
            buffW.write("List<" + classe.getNomeClasse() + "> lista" + classe.getNomeClasse() + " = new ArrayList<Pessoa>();");
            buffW.newLine();
            buffW.write("StringBuilder sql = new StringBuilder();");
            buffW.newLine();
            buffW.write("sql.append(\"SELECT * FROM " + classe.getTabela() + "\");");
            buffW.newLine();
            buffW.write("try {");
            buffW.newLine();
            buffW.write("pstmt = connection.prepareStatement(sql.toString());");
            buffW.newLine();
            buffW.write("rs = pstmt.executeQuery();");
            buffW.newLine();
            buffW.write("while (rs.next()) {");
            buffW.newLine();
            buffW.write(classe.getNomeClasse() + " " + classe.getNomeClasse().toLowerCase() + "= new " + classe.getNomeClasse() + "();");
            buffW.newLine();
            for (Atributos item : classe.getAtributos()) {
                buffW.write(classe.getNomeClasse().toLowerCase() + ".set" + item.getNome().substring(0, 1).toUpperCase().concat(item.getNome().substring(1)) + "(" + retornaTipoRs(item.getTipo().getDescricao()) + "(\""+item.getNome().toLowerCase()+"\"));");
                buffW.newLine();
            }
            buffW.write("lista" + classe.getNomeClasse()+".add("+classe.getNomeClasse().toLowerCase()+");");
            buffW.newLine();
            buffW.write("}");
            buffW.newLine();
            buffW.write("} catch (SQLException ex) {");
            buffW.newLine();
            buffW.write("Logger.getLogger(" + classe.getNomeClasse() + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
            buffW.newLine();
            buffW.write("}");
            buffW.newLine();
            buffW.write("return lista" + classe.getNomeClasse() + ";");
            buffW.newLine();
            buffW.write("}");
            buffW.newLine();
            buffW.write(
                    "}");
            buffR.close();

            buffW.close();

            System.out.println(
                    "Classe Gerada");
        } catch (Exception e) {
        }
    }

    public static String retornaTipoPstmt(String tipo) {
        if (tipo.equals("String")) {
            return "pstmt.setString";
        } else if (tipo.equals("Integer")) {
            return "pstmt.setInt";
        }
        return "";
    }

    public static String retornaTipoRs(String tipo) {
        if (tipo.equals("String")) {
            return "rs.getString";
        } else if (tipo.equals("Integer")) {
            return "rs.getInt";
        }
        return "";
    }
    
    public static String geraImports(){
        StringBuilder imports = new StringBuilder();
        imports.append("import java.io.Serializable;\n");
        imports.append("import java.sql.Connection;\n");
        imports.append("import java.sql.PreparedStatement;\n");
        imports.append("import java.sql.SQLException;\n");
        imports.append("import java.util.logging.Level;\n");
        imports.append("import java.util.logging.Logger;\n");
        imports.append("import java.util.ArrayList;\n");
        imports.append("import java.util.List;\n");
        imports.append("import java.sql.ResultSet;\n");
        return imports.toString();
    }
}

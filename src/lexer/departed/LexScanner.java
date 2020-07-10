package lexer.departed;

import java.io.*;

@Deprecated
public class LexScanner {
    private FileReader fr = null;
    private FileWriter fw = null;
    private PushbackReader pr = null;
    private String outMidFileName = "";

    LexScanner(String fileName){
        File fin = new File((fileName));

        // 文件必须以.c结尾
        if(!fin.getName().endsWith(".c")){
            System.err.println("文件不是以.c结尾的c语言文件，请重新选择文件");
            System.exit(-1);
        }

        // 尝试读取文件
        try {
            fr = new FileReader(fin);
            pr = new PushbackReader(fr,2);  // 两个缓冲字符
        } catch (FileNotFoundException e) {
            System.err.println("未找到"+fileName+"文件，请重新输入");
            e.printStackTrace();
        }

        outMidFileName = fileName.substring(0,fileName.indexOf('.'))+".lex";    // 词法分析结果后缀lex
        File fout = new File(outMidFileName);

        try {
            if(!fout.exists())
                fout.createNewFile();
            fw = new FileWriter(fout);
        } catch (IOException e) {
            System.err.println("写入文件错误");
            e.printStackTrace();
        }

    }

    public FileReader getFr() {
        return fr;
    }

    public FileWriter getFw() {
        return fw;
    }

    public PushbackReader getPr() {
        return pr;
    }

    public String getOutMidFileName() {
        return outMidFileName;
    }

    public boolean closeStream(){
        try {
            fw.close();
            return true;
        } catch (IOException e) {
            System.err.println("关闭流错误！");
            e.printStackTrace();
        }
        return false;
    }

}

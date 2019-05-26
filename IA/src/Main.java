import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;

class Main {

    private final static String PCG_MODEL = "englishPCFG.ser.gz";

    private final static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    private final static LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);


    public static Tree parse(String str) {
        /*
         * parser.setOptionFlags("-outputFormat", "wordsAndTags",
         * "-outputFormat", "penn",
         * "-outputFormat", "typedDependencies",
         * "-retainTMPSubcategories");
         */

        /*
         * A partir dos tokens, aplica o parser
         */

        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }

    private static List<CoreLabel> tokenize(String str) {

        /*
         * Quebra o texto em tokens
         */

        Tokenizer<CoreLabel> tokenizer =
                tokenizerFactory.getTokenizer(
                        new StringReader(str));
        return tokenizer.tokenize();
    }

    public static void main(String[] args) {
/*
        if(args.length == 0) {
            System.out.println("Falta o arquivo de entrada");
            return;
        }

*/
        String str = "The strongest rain ever recorded in India shut down the financial hub of Mumbai, snapped communication lines, closed airports and forced thousands of people to sleep in their offices or walk home during the night, officials said today.";


/*
        InputStream is;
        try {
            is = new FileInputStream(args[0]);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            buf.close();
            str = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


         * A partir daqui, str supostamente tem o texto
         */



        if(str.isEmpty()) {
            return;
        }


        /*
         * Aplica o parser e printa a arvore e as relacoes
         */

        Tree tree = parse(str);
        tree.pennPrint();

        TreePrint tp = new TreePrint("typedDependenciesCollapsed");
        tp.printTree(tree);

        //System.out.println();
        //TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        //GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        //GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        //Collection tdl = gs.typedDependenciesCollapsed();
        //System.out.println(tdl);
        //System.out.println();

        /*
         */



        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("relacoes");
            PrintWriter pw = new PrintWriter(fileWriter);
            tp.printTree(tree, pw);
        } catch (IOException e) {
            e.printStackTrace();
        }



        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("arvore"));
            writer.write(tree.pennString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
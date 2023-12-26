package image;

//  '▇', '●', '◉', '◍', '◎', '○', '☉', '◌',
public class TextColorShemaIOSimpl implements TextColorSchema {
    private char[] symbols = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌'};

    @Override
    public char convert(int color) {
        return (color < 32) ? '▇' :
                (color < 64) ? '●' :
                        (color < 96) ? '◉' :
                                (color < 128) ? '◍' :
                                        (color < 160) ? '◎' :
                                                (color < 192) ? '○' :
                                                        (color < 224) ? '☉' : '◌';
    }

//    public char convert(int color) {
//        return symbols[(int) Math.floor(color / 256. * symbols.length)];
//    }
}

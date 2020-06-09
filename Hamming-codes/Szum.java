import java.io.FileOutputStream;
import java.io.IOException;

public class Szum {

    public static void main(String[] args) throws IOException {
        if (args.length < 3) throw new IllegalArgumentException("Required 3 arguments!");

        double p = Double.parseDouble(args[0]);
        String input = args[1];
        String output = args[2];

        Converter converter = new Converter();
        byte[] fileAsBytes = converter.convertFileToByteArray(input);
        StringBuilder str = new StringBuilder();

        for (byte b : fileAsBytes) {
            for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
                if (Math.random() <= p) {
                    int bit = Koder.getBit(b, bitIndex);
                    if (bit == 0) {
                        b |= (1 << bitIndex);  // set a bit to 1
                    } else {
                        b &= ~(1 << bitIndex); // set a bit to 0
                    }
                }
            }
            for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
                str.append(Koder.getBit(b, bitIndex));
            }
        }

        byte[] bytes = String.valueOf(str).getBytes();
        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(bytes);
        }

    }

}

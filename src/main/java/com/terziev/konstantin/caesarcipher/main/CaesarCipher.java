package com.terziev.konstantin.caesarcipher.main;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaesarCipher {

    private static boolean isPlainText(@NonNull final char alphabetStart,
                                       @NonNull final char alphabetEnd,
                                       @NonNull final char character) {
        return alphabetStart <= character && alphabetEnd >= character;
    }

    private static String cipher(@NonNull final String text, @NonNull final long offset) {
        final var result = new StringBuilder();
        final var alphabetSize = 26;

        for (final var character : text.toCharArray()) {
            final var alphabetStart = Character.isLowerCase(character) ? 'a' : 'A';
            final var alphabetEnd = Character.isLowerCase(character) ? 'z' : 'Z';

            if (isPlainText(alphabetStart, alphabetEnd, character)) {
                final var usualPosition = character - alphabetStart;
                final var newPosition = (int) ((usualPosition + Math.abs(offset)) % alphabetSize);
                final var newCharacter = (char) (alphabetStart + newPosition);

                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String encrypt(@NonNull final String text, @NonNull final long offset) {
        return cipher(text, offset);
    }

    public static String decrypt(@NonNull final String text, @NonNull final long offset) {
        final var alphabetSize = 26;

        // cipher with complementory offset
        return cipher(text, alphabetSize - (offset % alphabetSize));
    }

    public static void main(@NonNull final String[] args) {
        final var appName = "caesar-cipher <TEXT>";
        final var header = "Encrypt and decrypt ASCII plaintext with Caesar Cipher\nEncrypts by default and keeps text case";
        final var footer = "\nPlease report issues at https://github.com/konstantindt/caesar-cipher/issues\nCopyright (c) 2021 Konstantin Terziev";
        final var defaultOffset = "3";
        final var options = new Options();
        final var encryptOption = Option.builder("e")
            .longOpt("encrypt")
            .required(false)
            .optionalArg(true)
            .type(Long.class)
            .desc("encrypt with number of positions to rotate alphabet (default = 3)")
            .build();
        final var decryptOption = Option.builder("d")
            .longOpt("decrypt")
            .required(false)
            .optionalArg(true)
            .type(Long.class)
            .desc("decrypt with number of positions to rotate alphabet (default = 3)")
            .build();
        final var helpOption = Option.builder("h")
            .longOpt("help")
            .required(false)
            .hasArg(false)
            .desc("prints this message")
            .build();
        final var parser = new DefaultParser();
        final var helpFormatter = new HelpFormatter();

        options.addOption(encryptOption);
        options.addOption(decryptOption);
        options.addOption(helpOption);

        try {
            final var cmd = parser.parse(options, args);
            final var inputs = cmd.getArgList();
            final var text = CollectionUtils.isNotEmpty(inputs) ? inputs.get(0) : null;
            final var noOptions = !cmd.iterator().hasNext();

            if (StringUtils.isEmpty(text)) {
                System.err.println("Missing text");
                System.exit(1);
            } else if (noOptions || cmd.hasOption(encryptOption.getOpt())) {
                final var encryptedText = encrypt(
                    text,
                    Long.valueOf((!noOptions)
                        ? cmd.getOptionValue(encryptOption.getOpt(), defaultOffset)
                        : defaultOffset)
                );
                System.out.println(encryptedText);
            } else if (cmd.hasOption(decryptOption.getOpt())) {
                final var decryptedText = decrypt(
                    text,
                    Long.valueOf(cmd.getOptionValue(decryptOption.getOpt(), defaultOffset))
                );
                System.out.println(decryptedText);
            } else if (cmd.hasOption(helpOption.getOpt())) {
                helpFormatter.printHelp(appName, header, options, footer, true);
            } else {
                System.err.println("Not supported");
                System.exit(2);
            }
            System.exit(0);
        } catch (ParseException | NullPointerException | NumberFormatException e) {
            System.err.println("Bad command");
            System.exit(3);
        }
    }

}

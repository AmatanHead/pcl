package com.github.amatanhead.pcl.lexer;

import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import static com.github.amatanhead.pcl.utils.Utils.streamreaderToString;

/**
 * Abstract text lexer provides basic facilities for handling the encoding-aware text input.
 * That includes the default encoding management, all conversions, standard constructors.
 */
public abstract class AbstractTextLexer implements TextLexer {
    private CharsetDecoder decoder;

    /**
     * Construct tokenizer using utf-8 as a default encoding.
     */
    public AbstractTextLexer() {
        setDefaultEncoding("utf-8");
    }

    /**
     * Construct tokenizer using the given encoding as a default encoding.
     *
     * @param charsetName name of the {@link Charset charset} which will be used by default.
     * @throws java.nio.charset.UnsupportedCharsetException given name doesn't match any known charset.
     */
    public AbstractTextLexer(String charsetName) {
        setDefaultEncoding(charsetName);
    }

    /**
     * Construct tokenizer using the given encoding as a default encoding.
     *
     * @param charset {@link Charset charset} which will be used by default.
     */
    public AbstractTextLexer(Charset charset) {
        setDefaultEncoding(charset);
    }

    /**
     * Construct tokenizer using the given encoding as a default encoding.
     *
     * @param decoder {@link CharsetDecoder decoder} which will be used by default.
     */
    public AbstractTextLexer(CharsetDecoder decoder) {
        setDefaultEncoding(decoder);
    }

    @Override
    public void setDefaultEncoding(String charsetName) {
        if (charsetName == null) {
            throw new NullPointerException("charset name must not be null");
        }

        setDefaultEncoding(Charset.forName(charsetName));
    }

    @Override
    public void setDefaultEncoding(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset must not be null");
        }

        setDefaultEncoding(charset.newDecoder());
    }

    @Override
    public void setDefaultEncoding(CharsetDecoder decoder) {
        if (decoder == null) {
            throw new NullPointerException("decoder must not be null");
        }

        this.decoder = decoder;
    }

    @Override
    public String getDefaultEncoding() {
        return decoder.charset().name();
    }

    @Override
    public TokenStream tokenize(InputStream inputStream) throws java.io.IOException, TokenizationError {
        return tokenize(inputStream, decoder);
    }

    @Override
    public TokenStream tokenize(InputStream inputStream, String charsetName) throws java.io.IOException, TokenizationError {
        return tokenize(inputStream, Charset.forName(charsetName));
    }

    @Override
    public TokenStream tokenize(InputStream inputStream, Charset charset) throws java.io.IOException, TokenizationError {
        return tokenize(inputStream, charset.newDecoder());
    }

    @Override
    public TokenStream tokenize(InputStream inputStream, CharsetDecoder decoder) throws java.io.IOException, TokenizationError {
        return tokenize(new InputStreamReader(inputStream, decoder));
    }

    @Override
    public TokenStream tokenize(InputStreamReader inputStreamReader) throws java.io.IOException, TokenizationError {
        return tokenize(streamreaderToString(inputStreamReader));
    }

    @Override
    public abstract TokenStream tokenize(String text) throws TokenizationError;
}

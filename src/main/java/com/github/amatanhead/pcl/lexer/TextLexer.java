package com.github.amatanhead.pcl.lexer;

import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Base interface for an encoding-aware text lexer.
 * <p>
 * Unlike raw byte lexers, text lexers treat theirs input like a text in the given encoding.
 * <p>
 * Note: every lexer should yield the standard EOF token at the end of its stream.
 */
public interface TextLexer extends Lexer {
    /**
     * Set the encoding to be used as a default one.
     * <p>
     * The default encoding will be used to convert raw {@link InputStream} in the {@link #tokenize(InputStream)} method.
     *
     * @param charsetName name of the {@link Charset charset} which will be used by default.
     * @throws java.nio.charset.UnsupportedCharsetException given name doesn't match any known charset.
     */
    void setDefaultEncoding(String charsetName);

    /**
     * Set the encoding to be used as a default one.
     * <p>
     * The default encoding will be used to convert raw {@link InputStream} in the {@link #tokenize(InputStream)} method.
     *
     * @param charset {@link Charset charset} which will be used by default.
     */
    void setDefaultEncoding(Charset charset);

    /**
     * Set the decoder to be used as a default one.
     * <p>
     * The default decoder will be used to convert raw {@link InputStream} in the {@link #tokenize(InputStream)} method.
     *
     * @param decoder {@link CharsetDecoder decoder} which will be used by default.
     */
    void setDefaultEncoding(CharsetDecoder decoder);

    /**
     * Returns the name of the character encoding being used by this lexer as a default one.
     * <p>
     * Note that historical names are ignored.
     * <p>
     * Also note that if the default encoding was set via the {@link #setDefaultEncoding(String)} method,
     * then the returned name may differ from the name passed to this method.
     *
     * @return the canonical name of the character encoding being used by this lexer as a default one.
     */
    String getDefaultEncoding();

    /**
     * Tokenize raw input using the default encoding.
     *
     * @param inputStream stream to be tokenized.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws java.io.IOException if an I/O error occurs.
     * @throws TokenizationError   if given stream cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(InputStream inputStream) throws java.io.IOException, TokenizationError;

    /**
     * Tokenize raw input using the given encoding.
     *
     * @param inputStream stream to be tokenized.
     * @param charsetName name of the {@link Charset charset} which will be used to decode this stream.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws java.io.IOException if an I/O error occurs.
     * @throws TokenizationError   if given stream cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(InputStream inputStream, String charsetName) throws java.io.IOException, TokenizationError;

    /**
     * Tokenize raw input using the given encoding.
     *
     * @param inputStream stream to be tokenized.
     * @param charset     {@link Charset charset} which will be used to decode this stream.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws java.io.IOException if an I/O error occurs.
     * @throws TokenizationError   if given stream cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(InputStream inputStream, Charset charset) throws java.io.IOException, TokenizationError;

    /**
     * Tokenize raw input using the given encoding.
     *
     * @param inputStream stream to be tokenized.
     * @param decoder     {@link CharsetDecoder decoder} instance which will be used to decode this stream.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws java.io.IOException if an I/O error occurs.
     * @throws TokenizationError   if given stream cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(InputStream inputStream, CharsetDecoder decoder) throws java.io.IOException, TokenizationError;

    /**
     * Tokenize text input.
     *
     * @param inputStreamReader text stream to be tokenized.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws java.io.IOException if an I/O error occurs.
     * @throws TokenizationError   if given stream cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(InputStreamReader inputStreamReader) throws java.io.IOException, TokenizationError;

    /**
     * Tokenize text input.
     *
     * @param text text to be tokenized.
     * @return the result of tokenization, i.e. a stream of tokens.
     * @throws TokenizationError if given string cannot be tokenized, i.e. it contains a malformed text.
     */
    TokenStream tokenize(String text) throws TokenizationError;
}

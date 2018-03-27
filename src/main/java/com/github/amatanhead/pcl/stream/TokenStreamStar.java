package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.errors.BookmarkError;

/**
 * TokenStreamStar keeps track of all previously generated tokens, allowing to backtrack as many tokens
 * as you like.
 * <p>
 * There is also a special mechanism for bookmarking and restoring stream position.
 */
public interface TokenStreamStar extends TokenStream1 {
    /**
     * Abstract base for bookmarks. Just to distinguish bookmark objects from other objects.
     */
    abstract class Bookmark {
        final TokenStreamStar originalStream;

        /**
         * A standard constructor.
         *
         * @param originalStream a stream instance which rendered this bookmark. This instance will be used
         *                       to ensure safety in {@link TokenStreamStar#assertBookmark(Bookmark)}. That is,
         *                       a bookmark can only be used with the stream instance which generated it.
         */
        protected Bookmark(TokenStreamStar originalStream) {
            this.originalStream = originalStream;
        }
    }

    /**
     * Make bookmark for the current stream position.
     *
     * @return a bookmark which can be supplied to the {@link #restoreBookmark(Bookmark)} method.
     */
    Bookmark makeBookmark();

    /**
     * Restore stream position at the given bookmark.
     *
     * @param bookmark bookmark for the stream state generated with {@link #makeBookmark()}.
     * @throws BookmarkError incorrect bookmark passed. See {@link #assertBookmark(Bookmark)}.
     * @apiNote this method can only accept bookmarks that were generated in this exact stream instance.
     */
    void restoreBookmark(Bookmark bookmark);

    /**
     * Assert that the given bookmark can be used with this stream.
     *
     * @param bookmark bookmark that needs checking.
     * @throws BookmarkError passed bookmark was generated in a different stream
     */
    default void assertBookmark(Bookmark bookmark) {
        if (bookmark.originalStream != this) {
            throw new BookmarkError("passed bookmark was generated in a different stream");
        }
    }
}

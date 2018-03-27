package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.errors.BookmarkError;
import com.github.amatanhead.pcl.errors.LexerProtocolError;
import com.github.amatanhead.pcl.errors.TokenizationError;


/**
 * Provides default implementations for {@link #makeBookmark()}/{@link #restoreBookmark(Bookmark)} for star-parsers.
 * <p>
 * Subclasses should call {@link #onInput()}/{@link #onUnput()} after each successful input/unput.
 * <p>
 * Also, subclasses should call {@link #markUnclear()} method in the beginning of the overloaded {@link #input()}
 * (even though they don't have to because it is called in the {@link #onInput()}).
 *
 * @implNote This abstract class keeps count of the current token index, starting with 0. On each input/unput this
 * index is changed. It's class user responsibility to notify this class about index change. This is done via
 * {@link #onInput()}/{@link #onUnput()}. Each bookmark consists of an index that was current in the moment
 * of bookmark's creation. When {@link #restoreBookmark(Bookmark)} is called, this class executes {@link #input()} or
 * {@link #unput()} `abs(bookmark.index - currentIndex)` times.
 */
abstract class StandardUnclearableBookmarkable extends StandardUnclearable implements TokenStreamStar {
    private static class StandardBookmark extends TokenStreamStar.Bookmark {
        final int position;

        protected StandardBookmark(TokenStreamStar originalStream, int position) {
            super(originalStream);
            this.position = position;
        }
    }

    private int position = 0;

    @Override
    public Bookmark makeBookmark() {
        return new StandardBookmark(this, position);
    }

    @Override
    public void restoreBookmark(Bookmark bookmark) {
        assertBookmark(bookmark);

        if (bookmark instanceof StandardBookmark) {
            StandardBookmark standardBookmark = (StandardBookmark) bookmark;

            while (standardBookmark.position > position) {
                try {
                    input();
                } catch (TokenizationError tokenizationError) {
                    throw new LexerProtocolError("failed to restore bookmark", tokenizationError);
                }
            }

            while (standardBookmark.position < position) {
                unput();
            }
        } else {
            throw new BookmarkError("wrong bookmark type");
        }
    }

    /**
     * Part of the abstract class API, this method should be called on each successful input.
     */
    protected void onInput() {
        markUnclear();
        position += 1;
    }

    /**
     * Part of the abstract class API, this method should be called on each successful unput.
     */
    protected void onUnput() {
        position -= 1;

        if (position < 0) {
            throw new LexerProtocolError("position became negative after calling unput");
        }
    }
}

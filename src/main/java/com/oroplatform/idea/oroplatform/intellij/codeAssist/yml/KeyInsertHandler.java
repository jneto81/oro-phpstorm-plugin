package com.oroplatform.idea.oroplatform.intellij.codeAssist.yml;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

class KeyInsertHandler implements InsertHandler<LookupElement> {
    static final InsertHandler<LookupElement> INSTANCE = new KeyInsertHandler();
    private static final String KEY_COLON = ": ";

    @Override
    public void handleInsert(InsertionContext context, LookupElement item) {
        final Document document = context.getDocument();
        final Editor editor = context.getEditor();

        int firstCharPos = firstCharPosition(document, context.getTailOffset());
        int lastCharPos = lastCharPositionIgnoringSpace(document, context.getTailOffset());
        final char lastChar = document.getCharsSequence().charAt(lastCharPos);
        final char firstChar = document.getCharsSequence().charAt(firstCharPos);
        if(lastChar != ':') {
            final int tailPos = getTailPos(context.getTailOffset(), lastChar);
            final String padText = isQuoteChar(firstChar) && !isQuoteChar(lastChar) ? firstChar+KEY_COLON : KEY_COLON;
            document.insertString(tailPos, padText);
            editor.getCaretModel().moveToOffset(tailPos + padText.length());
        } else {
            editor.getCaretModel().moveToOffset(lastCharPositionIgnoringSpace(document, lastCharPos + 1));
        }

//        TODO: turn off because more sophisticated logic here is needed. AutoPopup should be shown only for choices and references
//        AutoPopupController.getInstance(editor.getProject()).scheduleAutoPopup(editor);
    }

    private int firstCharPosition(Document document, int tailOffset) {
        CharSequence charsSequence = document.getCharsSequence();

        for(int i=tailOffset; i>1; i--) {
            char charAt = charsSequence.charAt(i-1);
            if(Character.isWhitespace(charAt)) {
                return i;
            }
        }

        return tailOffset;
    }

    private int getTailPos(int pos, char lastChar) {
        return pos + (isQuoteChar(lastChar) ? 1 : 0);
    }

    private boolean isQuoteChar(char lastChar) {
        return lastChar == '\'' || lastChar == '"';
    }

    private int lastCharPositionIgnoringSpace(Document document, int tailOffset) {
        CharSequence charsSequence = document.getCharsSequence();

        for(int i=tailOffset; i<charsSequence.length(); i++) {
            char charAt = charsSequence.charAt(i);
            if(charAt != ' ') {
                return i;
            }
        }

        return Math.min(tailOffset, charsSequence.length() - 1);
    }
}

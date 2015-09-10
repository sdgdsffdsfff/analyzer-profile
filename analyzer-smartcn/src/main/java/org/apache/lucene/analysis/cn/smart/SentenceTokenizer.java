package org.apache.lucene.analysis.cn.smart;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;

/**
 * Tokenizes input text into sentences.
 * <p>
 * The output tokens can then be broken into words with {@link WordTokenFilter}
 * </p>
 * @lucene.experimental
 * @deprecated Use {@link HMMChineseTokenizer} instead
 */
@Deprecated
public final class SentenceTokenizer extends Tokenizer {

	/**
	 * End of sentence punctuation: 。，！？；,!?;
	 */
	private final static String PUNCTION = "。，！？；,!?;";

	private final StringBuilder buffer = new StringBuilder();

	private int tokenStart = 0, tokenEnd = 0;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	public SentenceTokenizer() {
	}

	public SentenceTokenizer(AttributeFactory factory) {
		super(factory);
	}

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		buffer.setLength(0);
		int ci;
		char ch, pch;
		boolean atBegin = true;
		tokenStart = tokenEnd;
		ci = input.read();
		ch = (char) ci;

		while (true) {
			if (ci == -1) {
				break;
			} else if (PUNCTION.indexOf(ch) != -1) {
				// End of a sentence
				buffer.append(ch);
				tokenEnd++;
				break;
			} else if (atBegin && Utility.SPACES.indexOf(ch) != -1) {
				tokenStart++;
				tokenEnd++;
				ci = input.read();
				ch = (char) ci;
			} else {
				buffer.append(ch);
				atBegin = false;
				tokenEnd++;
				pch = ch;
				ci = input.read();
				ch = (char) ci;
				// Two spaces, such as CR, LF
				if (Utility.SPACES.indexOf(ch) != -1 && Utility.SPACES.indexOf(pch) != -1) {
					// buffer.append(ch);
					tokenEnd++;
					break;
				}
			}
		}
		if (buffer.length() == 0)
			return false;
		else {
			termAtt.setEmpty().append(buffer);
			offsetAtt.setOffset(correctOffset(tokenStart), correctOffset(tokenEnd));
			typeAtt.setType("sentence");
			return true;
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		tokenStart = tokenEnd = 0;
	}

	@Override
	public void end() throws IOException {
		super.end();
		// set final offset
		final int finalOffset = correctOffset(tokenEnd);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
}

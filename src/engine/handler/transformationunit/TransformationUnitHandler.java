/*
 * BSD-Lizenz Copyright (c) Teams of 'WPP Petrinetze' of HAW Hamburg 2010 -
 * 2013; various authors of Bachelor and/or Masterthesises --> see file
 * 'authors' for detailed information Weiterverbreitung und Verwendung in
 * nichtkompilierter oder kompilierter Form, mit oder ohne Veraenderung, sind
 * unter den folgenden Bedingungen zulaessig: 1. Weiterverbreitete
 * nichtkompilierte Exemplare muessen das obige Copyright, diese Liste der
 * Bedingungen und den folgenden Haftungsausschluss im Quelltext enthalten. 2.
 * Weiterverbreitete kompilierte Exemplare muessen das obige Copyright, diese
 * Liste der Bedingungen und den folgenden Haftungsausschluss in der
 * Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet
 * werden, enthalten. 3. Weder der Name der Hochschule noch die Namen der
 * Beitragsleistenden duerfen zum Kennzeichnen oder Bewerben von Produkten, die
 * von dieser Software abgeleitet wurden, ohne spezielle vorherige
 * schriftliche Genehmigung verwendet werden. DIESE SOFTWARE WIRD VON DER
 * HOCHSCHULE* UND DEN BEITRAGSLEISTENDEN OHNE JEGLICHE SPEZIELLE ODER
 * IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE UNTER ANDEREM
 * EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER SOFTWARE FUER
 * EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL SIND DIE HOCHSCHULE* ODER DIE
 * BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, ZUFAELLIGEN,
 * SPEZIELLEN, BEISPIELHAFTEN ODER FOLGESCHAEDEN (UNTER ANDEREM VERSCHAFFEN VON
 * ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER NUTZUNGSFAEHIGKEIT;
 * VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER GESCHAEFTSUNTERBRECHUNG),
 * WIE AUCH IMMER VERURSACHT UND UNTER WELCHER VERPFLICHTUNG AUCH IMMER, OB IN
 * VERTRAG, STRIKTER VERPFLICHTUNG ODER UNERLAUBTER HANDLUNG (INKLUSIVE
 * FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM WEG SIE AUCH IMMER DURCH DIE
 * BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, WENN SIE AUF DIE
 * MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: 1.
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of the
 * University nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission. THIS SOFTWARE IS PROVIDED BY THE UNIVERSITY* AND CONTRIBUTORS
 * AS IS AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY* OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. * bedeutet / means: HOCHSCHULE FUER ANGEWANDTE
 * WISSENSCHAFTEN HAMBURG / HAMBURG UNIVERSITY OF APPLIED SCIENCES
 */
package engine.handler.transformationunit;

import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import persistence.Persistence;
import transformation.TransformationUnit;
import transformation.units.utils.ExpressionGrammarLexer;
import transformation.units.utils.ExpressionGrammarParser;
import transformation.units.utils.TransformationUnitExecutionVisitor;
import engine.data.TransformationUnitData;
import engine.session.SessionManager;
import exceptions.EngineException;

public final class TransformationUnitHandler {

  private static TransformationUnitHandler instance;

  private SessionManager sessionManager;

  public static TransformationUnitHandler getInstance() {

    if (instance == null) {
      instance = new TransformationUnitHandler();
    }

    return instance;
  }

  private TransformationUnitHandler() {

    this.sessionManager = SessionManager.getInstance();
  }

  public int createTransformationUnit(String fileName, String filePath) {

    TransformationUnit transformationUnit = new TransformationUnit();

    return this.sessionManager.createTransformationUnitData(
      transformationUnit, fileName, filePath);
  }

  public void removeTransformationUnit(int transformationUnitId) {

    this.sessionManager.removeTransformationUnitData(transformationUnitId);
  }

  public String getFileName(int transformationUnitId) {

    return this.sessionManager.getTransformationUnitData(transformationUnitId).getFileName();
  }

  public void setControlExpression(int transformationUnitId,
    String controlExpression) {

    this.sessionManager.getTransformationUnitData(transformationUnitId).getTransformationUnit().setControlExpression(
      controlExpression);
  }

  public String getControlExpression(int transformationUnitId) {

    return this.sessionManager.getTransformationUnitData(transformationUnitId).getTransformationUnit().getControlExpression();
  }

  public void executeTransformationUnit(int transformationUnitId,
    int petrinetId, Map<String, Integer> ruleNameToId)
      throws EngineException {

    TransformationUnitData transformationUnitData =
      this.sessionManager.getTransformationUnitData(transformationUnitId);

    String expression =
      transformationUnitData.getTransformationUnit().getControlExpression();
    int asLongAsPossibleExecutionLimit =
      transformationUnitData.getAsLongAsPossibleExecutionLimit();
    int randomNumberOfTimesUpperRange =
      transformationUnitData.getRandomNumberOfTimesUpperRange();

    ANTLRInputStream inputStream = new ANTLRInputStream(expression);
    ExpressionGrammarLexer lexer = new ExpressionGrammarLexer(inputStream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);

    ExpressionGrammarParser parser = new ExpressionGrammarParser(tokenStream);

    ParseTree parseTree = parser.prog();

    TransformationUnitExecutionVisitor visitor =
      new TransformationUnitExecutionVisitor(petrinetId, ruleNameToId,
        asLongAsPossibleExecutionLimit, randomNumberOfTimesUpperRange);

    try {
      visitor.visit(parseTree);
    } catch (RuntimeException e) {
      // e.printStackTrace();
      throw new EngineException(
        "Die Transformationseinheit konnte nicht angewendet werden.");
    }

  }

  public void setAsLongAsPossibleExecutionLimit(int transformationUnitId,
    int executionLimit) {

    this.sessionManager.getTransformationUnitData(transformationUnitId).setAsLongAsPossibleExecutionLimit(
      executionLimit);
  }

  public void setRandomNumberOfTimesUpperRange(int transformationUnitId,
    int randomNumberOfTimesUpperRange) {

    this.sessionManager.getTransformationUnitData(transformationUnitId).setRandomNumberOfTimesUpperRange(
      randomNumberOfTimesUpperRange);
  }

  public void saveToFileSystem(int transformationUnitId)
    throws EngineException {

    TransformationUnitData data =
      this.sessionManager.getTransformationUnitData(transformationUnitId);

    TransformationUnit transformationUnit = data.getTransformationUnit();

    try {
      Persistence.saveTransformationUnit(transformationUnit,
        data.getFilePath());
    } catch (Exception e) {
      throw new EngineException(e.getMessage());
    }
  }

  public int loadFromFileSystem(String displayName, String filePath)
    throws EngineException {

    try {
      TransformationUnit transformationUnit =
        Persistence.loadTransformationUnit(filePath);
      return this.sessionManager.createTransformationUnitData(
        transformationUnit, displayName, filePath);
    } catch (Exception e) {
      throw new EngineException(e.getMessage());
    }
  }

  public int getAsLongAsPossibleExecutionLimit(int transformationUnitId) {

    return this.sessionManager.getTransformationUnitData(transformationUnitId).getAsLongAsPossibleExecutionLimit();
  }

  public int getRandomNumberOfTimesUpperRange(int transformationUnitId) {

    return this.sessionManager.getTransformationUnitData(transformationUnitId).getRandomNumberOfTimesUpperRange();
  }
}

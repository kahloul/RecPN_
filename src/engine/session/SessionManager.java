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

package engine.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.sun.istack.NotNull;

import petrinet.model.IArc;
import petrinet.model.INode;
import petrinet.model.Petrinet;
import transformation.Rule;
import transformation.TransformationUnit;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import engine.data.JungData;
import engine.data.PetrinetData;
import engine.data.RuleData;
import engine.data.SessionData;
import engine.data.TransformationUnitData;
import gui.graphLayout.AdvancedFRLayout;

/**
 * The instance manager manages all the different types of data objects like
 * {@link PetrinetData}, {@link JungData} etc. It has only a singleton
 * instance
 */
public final class SessionManager {

  /** Singleton instance */
  private static SessionManager instance;

  /** Counter for creating data IDs */
  private int idSessionData = 0;

  /** Stores petrinet data by their ID */
  private Map<Integer, PetrinetData> petrinetData;

  /** Stores rule data by their ID */
  private Map<Integer, RuleData> ruleData;

  /** Stores transformation unit data by their ID */
  private Map<Integer, TransformationUnitData> transformationUnitData;

  private Map<Integer, SessionData> sessionData;

  private static final double ATTRACTION_MULTIPLIER = 0.5;
  private static final double REPULSION_MULTIPLIER = 0.9;

  private SessionManager() {

    sessionData = new HashMap<Integer, SessionData>();
    petrinetData = new HashMap<Integer, PetrinetData>();
    ruleData = new HashMap<Integer, RuleData>();
    transformationUnitData = new HashMap<Integer, TransformationUnitData>();
  }

  /** Returns the singleton instace */
  public static SessionManager getInstance() {

    if (instance == null) {
      instance = new SessionManager();
    }

    return instance;
  }

  /**
   * Gets PetrinetData with the ID.
   *
   * @param id
   *        from the PetrinetData
   * @return the PetrinetData with this id, null if the Id is not known
   */
  public PetrinetData getPetrinetData(int id) {

    return petrinetData.get(id);
  }

  /**
   * This method return the RuleData for the Id
   *
   * @param id
   *        from the RuleData
   * @return the RuleData or null if the Id is not valid
   */
  public RuleData getRuleData(int id) {

    return ruleData.get(id);
  }

  /**
   * @param id
   * @return
   */
  public SessionData getSessionData(int id) {

    return sessionData.get(id);
  }

  /**
   * Create a new PetrinetData.
   *
   * @param empty
   *        petrinet for the PetrinetData
   * @return the new PetrinetData
   */
  public PetrinetData createPetrinetData(@NotNull Petrinet petrinet) {

    checkEmptyPetrinet(petrinet);

    PetrinetData data = new PetrinetData(getNextSessionDataId(), petrinet,
      getNewFRLayoutJungData());

    petrinetData.put(data.getId(), data);

    return data;
  }

  public void replacePetrinetData(int petrinetId, Petrinet petrinet,
    JungData petrinetJungData) {

    PetrinetData data = new PetrinetData(petrinetId, petrinet,
      petrinetJungData);

    petrinetData.put(petrinetId, data);
  }

  public int createTransformationUnitData(
    TransformationUnit transformationUnit, String fileName, String filePath) {

    TransformationUnitData data = new TransformationUnitData(
      transformationUnit, fileName, filePath);

    int dataId = getNextSessionDataId();

    transformationUnitData.put(dataId, data);

    return dataId;
  }

  public TransformationUnitData getTransformationUnitData(int id) {

    return transformationUnitData.get(id);
  }

  /**
   * Creates a new RuleData from all Petrinet (l, k, r).
   *
   * @param l
   *        id of left Petrinet
   * @param k
   *        id of middle Petrinet
   * @param r
   *        id of right Petrinet
   * @return the new RuleData
   */
  public RuleData createRuleData(@NotNull Rule rule) {

    checkEmptyRule(rule);

    RuleData data = new RuleData(getNextSessionDataId(), rule,
      getNewStaticLayoutJungData(), getNewStaticLayoutJungData(),
      getNewStaticLayoutJungData());

    ruleData.put(data.getId(), data);

    return data;
  }

  /**
   * Creates an empty JungLayout for a new NAC in a rule
   *
   * @param ruleData
   * @param nac
   */
  public void createJungLayoutForNac(@NotNull RuleData ruleData,
    @NotNull UUID nacId) {

    ruleData.putNacJungData(nacId, getNewStaticLayoutJungData());
  }

  /**
   * removes a Data from instance manager
   *
   * @param id
   *        of a SessionData
   * @return true if Data was successful closed, false if id was not found, or
   *         data coulnd't be closed
   */
  public boolean closeSessionData(int id) {

    return sessionData.remove(id) != null;
  }

  public boolean removeTransformationUnitData(int transformationUnitDataId) {

    boolean deleted = this.transformationUnitData.remove(
      transformationUnitDataId) != null;
    return deleted;
  }

  /**
   * @return an empty graph with a Static-Layout
   */
  private JungData getNewStaticLayoutJungData() {

    DirectedSparseGraph<INode, IArc> graph =
      new DirectedSparseGraph<INode, IArc>();

    return new JungData(graph, new StaticLayout<INode, IArc>(graph));
  }

  /**
   * @return an empty graph with a FR-Layout
   */
  private JungData getNewFRLayoutJungData() {

    DirectedSparseGraph<INode, IArc> graph =
      new DirectedSparseGraph<INode, IArc>();

    AdvancedFRLayout<INode, IArc> frLayout =
      new AdvancedFRLayout<INode, IArc>(graph);

    // Staerke der Federn: Abstossung und Anziehung ueber diese Werte
    // beeinflussbar
    frLayout.setAttractionMultiplier(ATTRACTION_MULTIPLIER);
    frLayout.setRepulsionMultiplier(REPULSION_MULTIPLIER);
    /* aax291 */

    return new JungData(graph, frLayout);
  }

  private int getNextSessionDataId() {

    idSessionData++;

    return idSessionData;
  }

  /**
   * throws an exception, if check result is negative.
   *
   * @param isValid
   *        if false, exception is thrown
   * @param message
   *        message of exception
   */
  private void check(boolean isValid, String message) {

    if (!isValid) {
      throw new IllegalArgumentException(message);
    }
  }

  /** Checks whether a petrinet is empty */
  private void checkEmptyPetrinet(Petrinet petrinet) {

    check(petrinet.getArcs().isEmpty(), "arcs have to be empty");
    check(petrinet.getPlaces().isEmpty(), "arcs have to be empty");
    check(petrinet.getTransitions().isEmpty(), "arcs have to be empty");
  }

  /** Checks whether a rule is empty */
  private void checkEmptyRule(Rule rule) {

    checkEmptyPetrinet(rule.getK());
    checkEmptyPetrinet(rule.getL());
    checkEmptyPetrinet(rule.getR());
  }
}

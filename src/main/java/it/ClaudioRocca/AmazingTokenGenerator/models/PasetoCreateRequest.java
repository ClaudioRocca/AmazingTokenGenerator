package it.ClaudioRocca.AmazingTokenGenerator.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * PasetoCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T18:57:23.680656+02:00[Europe/Rome]", comments = "Generator version: 7.8.0")
public class PasetoCreateRequest {

  private String sub;

  private String iss;

  private String aud;

  private Long exp;

  private Long nbf;

  private Long iat;

  public PasetoCreateRequest sub(String sub) {
    this.sub = sub;
    return this;
  }

  /**
   * Il soggetto del token
   * @return sub
   */
  
  @Schema(name = "sub", description = "Il soggetto del token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sub")
  public String getSub() {
    return sub;
  }

  public void setSub(String sub) {
    this.sub = sub;
  }

  public PasetoCreateRequest iss(String iss) {
    this.iss = iss;
    return this;
  }

  /**
   * L'emittente del token
   * @return iss
   */
  
  @Schema(name = "iss", description = "L'emittente del token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iss")
  public String getIss() {
    return iss;
  }

  public void setIss(String iss) {
    this.iss = iss;
  }

  public PasetoCreateRequest aud(String aud) {
    this.aud = aud;
    return this;
  }

  /**
   * I destinatari del token
   * @return aud
   */
  
  @Schema(name = "aud", description = "I destinatari del token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aud")
  public String getAud() {
    return aud;
  }

  public void setAud(String aud) {
    this.aud = aud;
  }

  public PasetoCreateRequest exp(Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * Timestamp di scadenza del token
   * @return exp
   */
  
  @Schema(name = "exp", description = "Timestamp di scadenza del token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exp")
  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }

  public PasetoCreateRequest nbf(Long nbf) {
    this.nbf = nbf;
    return this;
  }

  /**
   * Timestamp prima del quale il token non è valido
   * @return nbf
   */
  
  @Schema(name = "nbf", description = "Timestamp prima del quale il token non è valido", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nbf")
  public Long getNbf() {
    return nbf;
  }

  public void setNbf(Long nbf) {
    this.nbf = nbf;
  }

  public PasetoCreateRequest iat(Long iat) {
    this.iat = iat;
    return this;
  }

  /**
   * Timestamp di emissione del token
   * @return iat
   */
  
  @Schema(name = "iat", description = "Timestamp di emissione del token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iat")
  public Long getIat() {
    return iat;
  }

  public void setIat(Long iat) {
    this.iat = iat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasetoCreateRequest pasetoCreateRequest = (PasetoCreateRequest) o;
    return Objects.equals(this.sub, pasetoCreateRequest.sub) &&
        Objects.equals(this.iss, pasetoCreateRequest.iss) &&
        Objects.equals(this.aud, pasetoCreateRequest.aud) &&
        Objects.equals(this.exp, pasetoCreateRequest.exp) &&
        Objects.equals(this.nbf, pasetoCreateRequest.nbf) &&
        Objects.equals(this.iat, pasetoCreateRequest.iat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sub, iss, aud, exp, nbf, iat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasetoCreateRequest {\n");
    sb.append("    sub: ").append(toIndentedString(sub)).append("\n");
    sb.append("    iss: ").append(toIndentedString(iss)).append("\n");
    sb.append("    aud: ").append(toIndentedString(aud)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
    sb.append("    nbf: ").append(toIndentedString(nbf)).append("\n");
    sb.append("    iat: ").append(toIndentedString(iat)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


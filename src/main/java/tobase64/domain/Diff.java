package tobase64.domain;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Domain Object that holds base64 encoded binary data as strings, to be compared as part of a diff process.
 *
 * <p>Note: The primary key is going to be included in the API HTTP response bodies, for convenience.</p>
 */
@Entity
public class Diff {

    @Id
    @Column
    @ApiModelProperty(notes = "Id of Diff", name = "id", required = true)
    private Long id;

    @Column(name = "DIFF_LEFT")
    @ApiModelProperty(notes = "Id of Diff", name = "left", required = false)
    private String left;

    @Column(name = "DIFF_RIGHT")
    @ApiModelProperty(notes = "Right side of Diff", name = "right", required = false)
    private String right;

    public Diff() {
        super();
    }

    public Diff(Long id, String left, String right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

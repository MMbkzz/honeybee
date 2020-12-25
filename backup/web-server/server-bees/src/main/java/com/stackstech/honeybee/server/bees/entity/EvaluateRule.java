package com.stackstech.honeybee.server.bees.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class EvaluateRule extends AbstractAuditableEntity {
    private static final long serialVersionUID = 4240072518233967528L;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "evaluate_rule_id")
    @OrderBy("id ASC")
    private List<Rule> rules = new ArrayList<>();

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public EvaluateRule() {
    }

    public EvaluateRule(List<Rule> rules) {
        this.rules = rules;
    }
}


package com.mygdx.project.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Arrays;

public class MBSystem{
    private final ArrayList<MBComponent> nodes;
    private Action updateAction;
    private final Actor actor = new Actor();

    public MBSystem(MBComponent... nodes) {
        this.nodes  = new ArrayList<>(Arrays.asList(nodes));
        for (MBComponent node: this.nodes) {
            if(node.getSystem() != null) System.out.println("Node already belongs to a System!");
            node.setSystem(this);
        }
        updateAction = new Action() {
            @Override
            public boolean act(float v) {
                return true;
            }
        };
    }

    public void update(){
        actor.clearActions();
        actor.addAction(updateAction);
        actor.act(Gdx.graphics.getDeltaTime());
    }

    public void setUpdateAction(Action updateAction){
        this.updateAction = updateAction;
    }
    public void clear(){
        nodes.clear();
    }
    public void addNode(MBComponent node){
        nodes.add(node);
    }
    public void setNodes(MBComponent... nodes){
        this.nodes.clear();
        this.nodes.addAll(Arrays.asList(nodes));
    }
    public MBComponent getNode(int index){
        return nodes.get(index);
    }
}

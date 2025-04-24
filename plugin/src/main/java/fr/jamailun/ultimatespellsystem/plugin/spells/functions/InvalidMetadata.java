package fr.jamailun.ultimatespellsystem.plugin.spells.functions;

import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.MetadataNode;

public class InvalidMetadata extends RuntimeException {
    public InvalidMetadata(MetadataNode node, String message) {
        super("Illegal meta data node : " + node + ". " + message);
    }
}

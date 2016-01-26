/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api.impl.labelscan.reader;

import java.io.IOException;
import java.util.Iterator;

import org.neo4j.collection.primitive.PrimitiveLongIterator;
import org.neo4j.kernel.api.impl.index.partition.PartitionSearcher;
import org.neo4j.kernel.api.impl.labelscan.storestrategy.LabelScanStorageStrategy;
import org.neo4j.kernel.api.impl.schema.reader.IndexSearcherCloseException;
import org.neo4j.storageengine.api.schema.LabelScanReader;

/**
 * Label scan index reader that is able to read/sample a single partition of a partitioned index.
 */
public class SimpleLuceneLabelScanStoreReader implements LabelScanReader
{
    private final PartitionSearcher partitionSearcher;
    private final LabelScanStorageStrategy strategy;

    public SimpleLuceneLabelScanStoreReader( PartitionSearcher partitionSearcher, LabelScanStorageStrategy strategy )
    {
        this.partitionSearcher = partitionSearcher;
        this.strategy = strategy;
    }

    @Override
    public PrimitiveLongIterator nodesWithLabel( int labelId )
    {
        return strategy.nodesWithLabel( partitionSearcher.getIndexSearcher(), labelId );
    }

    @Override
    public Iterator<Long> labelsForNode( long nodeId )
    {
        return strategy.labelsForNode( partitionSearcher.getIndexSearcher(), nodeId );
    }

    @Override
    public void close()
    {
        try
        {
            partitionSearcher.close();
        }
        catch ( IOException e )
        {
            throw new IndexSearcherCloseException( e );
        }
    }
}
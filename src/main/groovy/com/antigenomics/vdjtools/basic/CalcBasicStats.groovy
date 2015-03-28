/*
 * Copyright 2013-2015 Mikhail Shugay (mikhail.shugay@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.antigenomics.vdjtools.basic

import com.antigenomics.vdjtools.Software
import com.antigenomics.vdjtools.sample.Sample
import com.antigenomics.vdjtools.sample.SampleCollection
import com.antigenomics.vdjtools.sample.metadata.MetadataTable

import static com.antigenomics.vdjtools.util.ExecUtil.formOutputPath

def cli = new CliBuilder(usage: "CalcBasicStats [options] " +
        "[sample1 sample2 sample3 ... if -m is not specified] output_prefix")
cli.h("display help message")
cli.m(longOpt: "metadata", argName: "filename", args: 1,
        "Metadata file. First and second columns should contain file name and sample id. " +
                "Header is mandatory and will be used to assign column names for metadata.")
cli.u(longOpt: "unweighted", "Will count each clonotype only once, apart from conventional frequency-weighted histogram.")

def opt = cli.parse(args)

if (opt == null)
    System.exit(-1)

if (opt.h || opt.arguments().size() == 0) {
    cli.usage()
    System.exit(-1)
}

// Check if metadata is provided

def metadataFileName = opt.m

if (metadataFileName ? opt.arguments().size() != 1 : opt.arguments().size() < 2) {
    if (metadataFileName)
        println "Only output prefix should be provided in case of -m"
    else
        println "At least 1 sample files should be provided if not using -m"
    cli.usage()
    System.exit(-1)
}

// Remaining arguments

def outputFilePrefix = opt.arguments()[-1],
    unweighted = (boolean) opt.u

def scriptName = getClass().canonicalName.split("\\.")[-1]

//
// Batch load all samples (lazy)
//

println "[${new Date()} $scriptName] Reading sample(s)"

def sampleCollection = metadataFileName ?
        new SampleCollection((String) metadataFileName) :
        new SampleCollection(opt.arguments()[0..-2])

println "[${new Date()} $scriptName] ${sampleCollection.size()} sample(s) prepared"

//
// Compute and output diversity measures, spectratype, etc
//

new File(formOutputPath(outputFilePrefix, "basicstats")).withPrintWriter { pw ->
    def header = "#$MetadataTable.SAMPLE_ID_COLUMN\t" +
            sampleCollection.metadataTable.columnHeader + "\t" +
            BasicStats.HEADER

    pw.println(header)

    def sampleCounter = 0

    sampleCollection.each { Sample sample ->
        def basicStats = new BasicStats(sample, !unweighted)

        println "[${new Date()} $scriptName] ${++sampleCounter} sample(s) processed"

        pw.println([sample.sampleMetadata.sampleId, sample.sampleMetadata, basicStats].join("\t"))
    }
}

println "[${new Date()} $scriptName] Finished"



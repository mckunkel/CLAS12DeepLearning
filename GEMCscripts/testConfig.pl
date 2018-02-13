#!/apps/bin/perl -w

#Script to make swif workflows
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('../config.yaml');

for $a ( @{ $config->{torusValue} } ) {
        for $b ( @{ $config->{solenoidValue} } ) {
                my $runworkflow = "swif run full_sim_tor" . $a . "sol"
                  . $b;

                print "$runworkflow \n";
                #system($runworkflow);
        }
}
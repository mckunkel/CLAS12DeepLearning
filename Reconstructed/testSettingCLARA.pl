#!/apps/bin/perl -w
### setCLARA.csh /work/clas12/mkunkel/myClara4a.8.2
#Script to run clara with decoded files
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('../config.yaml');

print "$config->{clas12Dir} \n";
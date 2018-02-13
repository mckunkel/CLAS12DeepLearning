#!/apps/bin/perl -w

#Script to make swif workflows
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('config.yaml');

my $workFlowID = $config->{projectName};
for $a ( @{ $config->{torusValue} } ) {
	for $b ( @{ $config->{solenoidValue} } ) {
		my $workflow = $workFlowID . "_tor" . $a . "sol" . $b;
		my $rmworkflow = "swif cancel $workflow -delete";
		print "$rmworkflow \n";

		system($rmworkflow);
	}
}

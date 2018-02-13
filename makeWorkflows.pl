#!/apps/bin/perl -w

#Script to make swif workflows
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config     = LoadFile('config.yaml');
my $workFlowID = $config->{projectName};
for $a ( @{ $config->{torusValue} } ) {
	for $b ( @{ $config->{solenoidValue} } ) {
		for my $fileName ( @{ $config->{fileName} } ) {

			my $workflow =
			  "-workflow " . $fileName . $workFlowID . "_tor" . $a . "sol" . $b;
			my $createWorkflow = "swif create $workflow";
			print "$createWorkflow \n";
			my $runworkflow = "swif run " . $workflow;
			print "$runworkflow \n";

			system($createWorkflow);
			system($runworkflow);

		}
	}
}

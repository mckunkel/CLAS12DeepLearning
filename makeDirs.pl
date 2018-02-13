#!/apps/bin/perl -w

#Script to make directories
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;

use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('config.yaml');

my $dataDir = "$config->{path}/$config->{projectName}";

for my $d ( @{ $config->{directories} } ) {
	for my $a ( @{ $config->{torusValue} } ) {
		for my $b ( @{ $config->{solenoidValue} } ) {

			my $torusSol_dir = "$dataDir/".$d."/Torus" . $a . "Sol" . $b;

			my $mkdir = "mkdir $torusSol_dir";
			print "$mkdir \n";
			system($mkdir);
		}
	}
}

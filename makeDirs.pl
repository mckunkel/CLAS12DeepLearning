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
			for my $fileName ( @{ $config->{fileName} } ) {

				my @commandList;

				my $torusSol_dir =
				    $dataDir . "/"
				  . $d
				  . "/Torus"
				  . $a . "Sol"
				  . $b . "/"
				  . $fileName;

				if ( !( -d $dataDir ) ) {
					#print "$dataDir does not exist! \n";
					push @commandList, "mkdir $dataDir";
				}
				if ( !( -d $dataDir . "/" . $d ) ) {
					#print $dataDir . "/" . $d . " does not exist! \n";
					my $tempDir = $dataDir . "/" . $d;
					push @commandList, "mkdir $tempDir";
				}
				if ( !( -d $dataDir . "/" . $d . "/Torus" . $a . "Sol" . $b ) )
				{
					#print $dataDir . "/" . $d . "/Torus" . $a . "Sol" . $b
					#  . " does not exist! \n";
					my $tempDir =
					  $dataDir . "/" . $d . "/Torus" . $a . "Sol" . $b;
					push @commandList, "mkdir $tempDir";
				}
				if ( !( -d $torusSol_dir ) ) {
					#print "$torusSol_dir does not exist! \n";
					push @commandList, "mkdir $torusSol_dir";

				}

				#my $mkdir = "mkdir $torusSol_dir";
				#print "$mkdir \n";
				print "@commandList\n";

				system(@commandList);
			}
		}
	}
}

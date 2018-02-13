#!/apps/bin/perl -w

#Script to find files the have lower size than average
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use POSIX;
use YAML::XS 'LoadFile';
use Data::Dumper;
my $config = LoadFile('../config.yaml');

my $nJobs      = 470;                                     # total number of jobs
my $submit_dir = "$config->{path}/$config->{projectName}";
my $fileName   = $config->{fileName};

my $deletedFile = 0;

#interate through torus
for my $a ( @{ $config->{torusValue} } ) {

	#interate through solenoid
	for my $b ( @{ $config->{solenoidValue} } ) {
		my $iJob      = 0;
		my $doneJobs  = 0;
		my $totalSize = 0;

		my $torusSol_dir = "Torus" . $a . "Sol" . $b;
		my $fileOutput_dir =
		  "$submit_dir/$config->{directories}->[0]/$torusSol_dir";

		#find average file size from all files in $fileOutput_dir
		while ( $iJob < $nJobs ) {
			my $file_out =
			    "$fileOutput_dir/$fileName"
			  . $a . "Sol"
			  . $b . "_"
			  . $iJob . ".ev";
			if ( -e $file_out ) {
				$doneJobs++;
				my $filesize = -s $file_out;
				$filesize = ceil( $filesize / ( 1024 * 1024 ) );
				$totalSize = $totalSize + $filesize;
			}
			$iJob++;
		}    #end of while loop
		     #now we have the size of all the files, lets find the average
		my $average = $totalSize / $doneJobs;
		print "Average is ", $average, "\n";

#re-interate in while loop for files in $fileOutput_dir to see if file is less than average, if so, delete it
		$iJob = 0;
		while ( $iJob < $nJobs ) {
			my $file_out =
			    "$fileOutput_dir/$fileName"
			  . $a . "Sol"
			  . $b
			  . "_"
			  . $iJob . ".ev";
			if ( -e $file_out ) {
				my $filesize = -s $file_out;
				$filesize = ceil( $filesize / ( 1024 * 1024 ) );
				if ( $filesize < 0.985 * $average ) {
					print "$file_out is to be deleted  \n";
					print "$filesize is fileSize and Average is  $average \n";
					my $rmFile = "rm $file_out";
					$deletedFile++;
					system($rmFile);
				}
			}
			$iJob++;
		}    #end of while loop
	}    #end of solenoid loop
}    #end of torus loops
print "number of deleted files is $deletedFile \n";

#!/apps/bin/perl -w
### setCLARA.csh /work/clas12/mkunkel/myClara4a.8.3
#Script to run clara with decoded files
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('../config.yaml');
####Some environment book keeping
print "CLARA_HOME is set to be $ENV{'CLARA_HOME'}\n";
print "COATJAVA is set to be $ENV{'COATJAVA'}\n";
print "CLAS12DIR is set to be $ENV{'CLAS12DIR'}\n";
print "\n";
############
my $submit_dir = "$config->{path}/$config->{projectName}";
my $clas12Dir  = $config->{clas12Dir};
my $userName   = $config->{userName};

my $claraSERVICE = "$ENV{'COATJAVA'}/config/services.yaml";    #Location of the service files i.e. the jars

print "$claraSERVICE\n";

my $nJobs   = $config->{NinitialJobs};      # total number of jobs
my $NEvents = $config->{NeventsPerJob};    #
my $session = 0;

#interate through torus
for $a ( @{ $config->{torusValue} } ) {
	for $b ( @{ $config->{solenoidValue} } ) {
		my $torusSol_dir = "Torus" . $a . "Sol" . $b;
		my $decoded_dir  = "$submit_dir/$config->{directories}->[1]/$torusSol_dir"
		  ;          #Location of your decoded hipo-files
		my $recon_dir = "$submit_dir/$config->{directories}->[2]/$torusSol_dir"
		  ;    #Location where the reconstructed hipo-file should be written

		#lets get the clara list
		my $claraList = "fileList" . $a . "Sol" . $b;

		open my $clara_file, ">$claraList.list"
		  or die "cannot open $claraList.list file:$!";

		#lets make a fileList with values not already reconstructed
		my $iJob = 0;
		while ( $iJob < $nJobs ) {
			my $decodedData =
			    $config->{fileName}
			  . $a . "Sol"
			  . $b . "_"
			  . $iJob . ".hipo";
			my $decoded_out = "$decoded_dir/$decodedData";
			my $reconData   = "out_"
			  . $config->{fileName}
			  . $a . "Sol"
			  . $b . "_"
			  . $iJob . ".hipo";
			my $recon_out = "$recon_dir/$reconData";

			if ( -e $decoded_out ) {
				#print "$recon_out This is the outfile \n";
				if ( -e $recon_out ) {

					#print "YO dumbass, are you overwriting an existing file? Tsk Tsk!";
					$iJob++;
					next;
				}

				print $clara_file "$decodedData\n";
			}
			$iJob++;
		}    #end of while loop
		close $clara_file;

		#system("ls $decoded_dir/* > $claraList.list");

		#Done making list

		my $claraCOOK = $claraList . "_cook.clara";
		open my $get_file, ">$claraCOOK"
		  or die "cannot open $claraCOOK file:$!";

		print $get_file "set inputDir $decoded_dir\n";
		print $get_file "set outputDir $recon_dir\n";
		print $get_file "\n";

		#print $get_file "set farm.disk 5\n";
		print $get_file "set farm.time 1440\n";

		#print $get_file "set farm.os centos7\n";
		#print $get_file "set farm.track analysis\n";
		#print $get_file "set farm.memory 37\n";
		print $get_file "set farm.cpu 32\n";
		print $get_file "set farm.scaling 4\n";
		print $get_file "set threads 32\n";
		print $get_file "set javaMemory 2\n";

		print $get_file "\n";

		print $get_file "set session s"
		  . $userName
		  . $config->{projectName}
		  . $session
		  . "Rec\n";
		print $get_file "set description d"
		  . $userName
		  . $config->{projectName}
		  . $session
		  . "Rec\n";
		print $get_file "set fileList $clas12Dir/$claraList.list\n";
		print $get_file "set servicesFile $claraSERVICE\n";

		print $get_file "run farm\n";

		close $get_file;

		my $runClara = "$ENV{'CLARA_HOME'}/bin/clara-shell $claraCOOK";

		system("$runClara");
		print "$runClara\n";
		$session++;

		#sleep(60);
	}    #end of solenoid loop
}    #end of torus loops

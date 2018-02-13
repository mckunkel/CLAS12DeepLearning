#!/apps/bin/perl -w

#Script to submit simulations, decoding and reconstruction to the farm
#Last date worked on> 08/24/17
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de

use strict;
use warnings;
use YAML::XS 'LoadFile';
use Data::Dumper;

my $config = LoadFile('../config.yaml');

my $nJobs   = $config->{NinitialJobs};      # total number of jobs
my $NEvents = $config->{NeventsPerJob};    #

#workflow settings
my $project   = "-project clas12";
my $track     = "-track simulation";
my $time      = "-time 9min";
my $OS        = "-os centos7";
my $shell     = "-shell /bin/tcsh";
my $ram       = "-ram 6g";
my $disk      = "-disk 2g";
my $CPU_count = "-cores 1";

#
#
################################
my $submit_dir    = "$config->{path}/$config->{projectName}";
my $workFlowID = $config->{projectName};

my $clara_dir    = $config->{claraConfig};
my $coatjava_dir = "$clara_dir/plugins/clas12";

my $command_source = "source setCLARA.csh $clara_dir";
my $input_0        = "-input setCLARA.csh $config->{topDir}/setCLARA.csh";

my $command_exit = "exit 0";

for $a ( @{ $config->{torusValue} } ) {
	for $b ( @{ $config->{solenoidValue} } ) {
		my $iJob = 0;

		my $torusSol_dir = "Torus" . $a . "Sol" . $b;
		my $workflow = "-workflow " . $workFlowID . "_tor" . $a . "sol" . $b;

		my $gemcInput_dir =
		  "$submit_dir/$config->{directories}->[0]/$torusSol_dir";
		my $decoded_dir =
		  "$submit_dir/$config->{directories}->[1]/$torusSol_dir";

		while ( $iJob < $nJobs ) {

			#check to see in gemc file already exists
			my $gemc_in =
			  $config->{fileName} . $a . "Sol" . $b . "_" . $iJob . ".ev";
			my $input_1 = "-input $gemc_in $gemcInput_dir/$gemc_in";

			my $decodedData =
			  $config->{fileName} . $a . "Sol" . $b . "_" . $iJob . ".hipo";

			my $decodedOut = "$decoded_dir/$decodedData";
			if ( -e $decodedOut ) {

		 #print "YO dumbass, are you overwriting an existing file? Tsk Tsk! \n";
				$iJob++;
				next;
			}

			my $doDecoding =
			    "$coatjava_dir/bin/evio2hipo -r 11 -t "
			  . $a . " -s "
			  . $b
			  . " -o $decodedData $gemc_in";
			my $mv_decoded = "-output $decodedData $decoded_dir/$decodedData";

			open my $command_file, ">command.dat"
			  or die "cannot open command.dat file:$!";
			print $command_file "$doDecoding; $command_exit";   #$command_source
			close $command_file;

			my $sub =
"swif add-job $workflow $project $track $time $OS $ram $disk $CPU_count $input_0 $input_1 -script command.dat $mv_decoded";
			system($sub);
			print "$sub \n\n";

			$iJob++;
		}    #end of job loop
	}    #end of solenoid loop
}    #end of torus loop

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
my $time      = "-time 400min";
my $OS        = "-os centos7";
my $shell     = "-shell /bin/tcsh";
my $ram       = "-ram 6g";
my $disk      = "-disk 4g";
my $CPU_count = "-cores 1";

#
#
################################

my $submit_dir    = "$config->{path}/$config->{projectName}";
my $lundInput_dir = "$submit_dir/$config->{lundDirectory}";
my $workFlowID    = $config->{projectName};

my $command_source =
  "source /group/clas12/gemc/environment.csh $config->{gemcConfig}"; 
my $command_exit = "exit 0";

#Options to input into Gemc
my $eventString    = "name=\"N\"              value=\"350\"";
my $setEventString = "name=\"N\"              value=\"$NEvents\"";
my $setEvents = "perl -p -i -e 's/$eventString/$setEventString/g' clas12.gcard";

#These will be set inside the torus and solenoid loop
my $torusString    = "value=\"clas12-torus-big, -1\"";
my $solenoidString = "value=\"clas12-solenoid, 1\"";

my $input_1 =
  "-input clas12.gcard $submit_dir/clas12_$config->{gemcConfig}.gcard";

for $a ( @{ $config->{torusValue} } ) {
	for $b ( @{ $config->{solenoidValue} } ) {
		my $iJob = 0;

		my $torusSol_dir = "Torus" . $a . "Sol" . $b;
		my $workflow = "-workflow " . $workFlowID . "_tor" . $a . "sol" . $b;
		my $gemcOutput_dir =
		  "$submit_dir/$config->{directories}->[0]/$torusSol_dir";

		#Options to input into Gemc
		my $setTorusString = "value=\"clas12-torus-big, " . $a . "\"";
		my $setTorus =
		  "perl -p -i -e 's/$torusString/$setTorusString/g' clas12.gcard";

		my $setSolenoidtString = "value=\"clas12-solenoid, " . $b . "\"";
		my $setSolenoid =
"perl -p -i -e 's/$solenoidString/$setSolenoidtString/g' clas12.gcard";

		print "$torusSol_dir and $workflow \n";

		while ( $iJob < $nJobs ) {

			#now lets get the lund file as input
			my $input_2 =
"-input $config->{gcardLundPrefix}.lund $lundInput_dir/$config->{lundPrefix}_$iJob.lund";

			#check to see in gemc file already exists
			my $gemc_out =
			    "$gemcOutput_dir/$config->{fileName}"
			  . $a . "Sol"
			  . $b . "_"
			  . $iJob . ".ev";
			my $mv_gemc = "-output out.ev $gemc_out";

			if ( -e $gemc_out ) {

		 #print "YO dumbass, are you overwriting an existing file? Tsk Tsk! \n";
				$iJob++;
				next;
			}
			my $gemc_run = "gemc clas12.gcard";

			open my $command_file, ">command.dat"
			  or die "cannot open command.dat file:$!";
			print $command_file
"$command_source ; $setEvents;  $setTorus; $setSolenoid; $gemc_run; $command_exit";

			close $command_file;

			my $sub =
"swif add-job $workflow $project $track $time $OS $ram $disk $CPU_count $input_1 $input_2 -script command.dat $mv_gemc";
			system($sub);
			print "$sub \n\n";

			$iJob++;
		}    #end of job loop
	}    #end of solenoid loop
}    #end of torus loop

#!/apps/bin/perl -w

#Script to decode GEMC files
#Written by: Michael C. Kunkel / m.kunkel@fz-juelich.de


use strict;
use warnings;
use POSIX;

my $nJobs = 4;	# total number of jobs 469
my $submit_dir = "/volatile/clas12/mkunkel/EtaPrimeDilepton";
my $clara_dir = "/work/clas12/mkunkel/myClara4a.8.1";
my $coatjava_dir = "$clara_dir/plugins/clas12";
my @torusValue = ("-0.75","-1.0","1.0");
my @solenoidValue = ("0.8");

#interate through torus
for $a (0 .. $#torusValue)
{
  #interate through solenoid
  for $b (0 .. $#solenoidValue){
    my $iJob = 0;
    
    my $torusSol_dir = "Torus".$torusValue[$a]."Sol".$solenoidValue[$b];
    my $gemcOutput_dir = "$submit_dir/GEMCFiles/$torusSol_dir";
    my $decoded_dir = "$submit_dir/DecodedFiles/$torusSol_dir";
    
    #find average file size from all files in $gemcOutput_dir
    while($iJob < $nJobs){
      my $gemc_out = "$gemcOutput_dir/EtaPrimeDilepton_Tor".$torusValue[$a]."Sol".$solenoidValue[$b]."_".$iJob.".ev";
      my $decodedData = "EtaPrimeDilepton_Tor".$torusValue[$a]."Sol".$solenoidValue[$b]."_".$iJob.".hipo";
      my $decoded_out = "$decoded_dir/$decodedData";
      
      if(-e $gemc_out){
        if(-e $decoded_out){
          #print "YO dumbass, are you overwriting an existing file? Tsk Tsk!";
          $iJob++;
          next;
        }
        my $doDecoding = "$coatjava_dir/bin/evio2hipo -r 11 -t ".$torusValue[$a]." -s ".$solenoidValue[$b]." -o $decoded_out $gemc_out";
        print "$doDecoding \n";
        system($doDecoding);
        
      }
      $iJob++;
    }#end of while loop
  }#end of solenoid loop
}#end of torus loops

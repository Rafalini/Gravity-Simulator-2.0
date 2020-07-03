package constants;
//Constants for movement mechanics, impacts, etc. Super parameters to adjust.
public class Constants {
    public static final double G_graviti_const = 0.0001;     //graviti const
    public static final double VelFactor = 0.01; //velocity normalization, to make arrows smaller
    public static final double Mfactor = 1000000; //mass normalization
    public static final double Mloss = 0.1; //10% mass loss on impact
    public static final double RadiusOnImpact = 1; //impact if dist <= max(R,r)*this
    public static final double StepFactor = 0.5;   //V*time*"this_factor"  to decrease movement speed
}